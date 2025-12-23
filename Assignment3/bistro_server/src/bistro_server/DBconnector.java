package bistro_server;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import entities.*;

public class DBconnector {

    private Connection conn;

    // date_of_placing_order stays DATE (yyyy-MM-dd)
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

    // client sends order datetime as: "yyyy-MM-dd HH:mm:ss"
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private HashMap<RequestType, RequestHandler> handlers;

    public DBconnector() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bistro?useSSL=false&serverTimezone=UTC",
                "root",
                "shonv2014!"
            );
            System.out.println("SQL connection succeeded");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        handlers = new HashMap<>();
        handlers.put(RequestType.WRITE_ORDER, this::addOrder);
        handlers.put(RequestType.READ_ORDER, this::getOrder);
        handlers.put(RequestType.READ_EMAIL, this::readEmail);
        handlers.put(RequestType.LOGIN_REQUEST, this::checkLogin);
        handlers.put(RequestType.REGISTER_REQUEST, this::addNewUser);
    }

    public String handleQueries(Object obj) {
        Request r = (Request) obj;
        RequestHandler handler = handlers.get(r.getType());
        if (handler == null) return "❌ No handler for request type: " + r.getType();
        return handler.handle(r);
    }

    /* ================= WRITE ORDER =================
       Uses ONLY a DATETIME column named: order_datetime

       IMPORTANT: This assumes your WriteRequest query is:
       INSERT INTO `order`
       (order_number, order_datetime, number_of_guests, confirmation_code, subscriber_id, date_of_placing_order, contact)
       VALUES (?, ?, ?, ?, ?, ?, ?)
    */
    private String addOrder(Request r) {
        Order o = ((WriteRequest) r).getOrder();

        try {
            int nextCode = getNextConfirmationCode(); // increments on server

            int subId = 0;
            try { subId = Integer.parseInt(o.getSubscriberId()); } catch (Exception ignored) {}

            String contact = (o.getContact() == null) ? "" : o.getContact().trim();
            if (contact.isEmpty()) return "❌ Missing contact (phone/email).";

            // Parse requested slot & normalize
            LocalDateTime requested = LocalDateTime
                    .parse(o.getOrderDateTime(), dateTimeFormatter)
                    .withSecond(0).withNano(0);

            // Guard: working hours 11:00 <= time < 22:00 (matches your client)
            if (!isWithinWorkingHours(requested.toLocalTime())) {
                return "❌ Selected time is outside working hours (11:00–22:00).";
            }

            // Exact slot check (date + time together)
            if (isSlotTaken(requested)) {
                String suggestions = getAvailableSlotsAround(requested, 2, 30);
                if (suggestions.isEmpty()) {
                    return "❌ This date and time is already taken.\nNo available slots found within ±2 hours.";
                }
                return "❌ This date and time is already taken.\nAvailable slots (±2 hours):\n" + suggestions;
            }

            PreparedStatement stmt = conn.prepareStatement(r.getQuery());

            stmt.setInt(1, nextCode);        // order_number
            stmt.setObject(2, requested);    // ✅ order_datetime (DATETIME) — no timezone conversion
            stmt.setInt(3, Integer.parseInt(o.getNumberOfGuests()));
            stmt.setInt(4, nextCode);        // confirmation_code

            // Guest => NULL subscriber_id
            if (subId == 0) stmt.setNull(5, Types.INTEGER);
            else stmt.setInt(5, subId);

            stmt.setDate(6, Date.valueOf(LocalDate.parse(o.getDateOfPlacingOrder(), dateFormatter)));
            stmt.setString(7, contact);

            int rows = stmt.executeUpdate();

            if (rows == 1) {
                return "✅ Order saved successfully!\nOrder Number: " + nextCode +
                       "\nConfirmation Code: " + nextCode;
            }
            return "❌ Order was not saved.";

        } catch (SQLIntegrityConstraintViolationException e) {
            // UNIQUE(order_datetime) race condition safety
            try {
                LocalDateTime requested = LocalDateTime
                        .parse(o.getOrderDateTime(), dateTimeFormatter)
                        .withSecond(0).withNano(0);
                String suggestions = getAvailableSlotsAround(requested, 2, 30);
                if (!suggestions.isEmpty()) {
                    return "❌ This date and time is already taken.\nAvailable slots (±2 hours):\n" + suggestions;
                }
            } catch (Exception ignored) {}
            return "❌ This date and time is already taken. Please choose another time.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error saving order.";
        }
    }

    /* ================= exact DATETIME slot check ================= */
    private boolean isSlotTaken(LocalDateTime requested) throws SQLException {
        String q = "SELECT 1 FROM `order` WHERE order_datetime = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setObject(1, requested.withSecond(0).withNano(0)); // ✅ LocalDateTime (DATETIME)
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /* ================= available-only suggestions ±2 hours ================= */
    private String getAvailableSlotsAround(LocalDateTime requested, int hoursRadius, int stepMinutes) throws SQLException {
        requested = requested.withSecond(0).withNano(0);
        LocalDate day = requested.toLocalDate();

        LocalDateTime from = requested.minusHours(hoursRadius);
        LocalDateTime to   = requested.plusHours(hoursRadius);

        // clamp to same day business hours: 11:00 <= t < 22:00
        LocalDateTime open  = LocalDateTime.of(day, LocalTime.of(11, 0));
        LocalDateTime close = LocalDateTime.of(day, LocalTime.of(22, 0)); // not inclusive

        if (from.isBefore(open)) from = open;

        // last candidate is 22:00 - stepMinutes (e.g., 21:30 for 30 mins)
        LocalDateTime lastCandidate = close.minusMinutes(stepMinutes);
        if (to.isAfter(lastCandidate)) to = lastCandidate;

        if (from.isAfter(to)) return "";

        Set<LocalDateTime> taken = getTakenSlotsInWindow(from, to);

        DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder sb = new StringBuilder();

        for (LocalDateTime t = from; !t.isAfter(to); t = t.plusMinutes(stepMinutes)) {
            LocalDateTime candidate = t.withSecond(0).withNano(0);

            if (!isWithinWorkingHours(candidate.toLocalTime())) continue;

            if (!taken.contains(candidate)) {
                sb.append("• ")
                  .append(candidate.toLocalDate())
                  .append(" ")
                  .append(candidate.toLocalTime().format(fmtTime))
                  .append("\n");
            }
        }

        // Limit message length (optional)
        String[] lines = sb.toString().split("\n");
        if (lines.length > 16) {
            StringBuilder limited = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                limited.append(lines[i]).append("\n");
            }
            return limited.toString();
        }

        return sb.toString();
    }

    private Set<LocalDateTime> getTakenSlotsInWindow(LocalDateTime from, LocalDateTime to) throws SQLException {
        String q = "SELECT order_datetime FROM `order` WHERE order_datetime BETWEEN ? AND ?";

        Set<LocalDateTime> set = new HashSet<>();

        try (PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setObject(1, from.withSecond(0).withNano(0)); // ✅ LocalDateTime
            stmt.setObject(2, to.withSecond(0).withNano(0));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime t = rs.getObject(1, LocalDateTime.class);
                    if (t != null) {
                        set.add(t.withSecond(0).withNano(0));
                    }
                }
            }
        }

        return set;
    }

    // matches your client time generation: 11:00 <= t < 22:00
    private boolean isWithinWorkingHours(LocalTime time) {
        LocalTime opening = LocalTime.of(11, 0);
        LocalTime closing = LocalTime.of(22, 0);
        return !time.isBefore(opening) && time.isBefore(closing);
    }

    private int getNextConfirmationCode() throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT IFNULL(MAX(confirmation_code), 0) + 1 FROM `order`")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    /* ================= READ ORDER =================
       Make sure your ReadRequest SELECT uses order_datetime as the 2nd column.
    */
    private String getOrder(Request r) {
        String query = r.getQuery();
        String orderNum = ((ReadRequest) r).getOrderNum();
        String result = "Results:\n";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, orderNum);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return "No order found.";

            do {
                result += "Order Number: " + rs.getString(1) + "\n";
                result += "Order DateTime: " + rs.getString(2) + "\n";
                result += "Guests: " + rs.getString(3) + "\n";
                result += "Confirmation Code: " + rs.getString(4) + "\n";
                result += "Subscriber ID: " + rs.getString(5) + "\n";
                result += "Placed On: " + rs.getString(6) + "\n";
            } while (rs.next());

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Error reading order.";
        }

        return result;
    }

    /* ================= READ EMAIL ================= */
    private String readEmail(Request r) {
        int subId = ((ReadEmailRequest) r).getSubscriberId();

        try {
            PreparedStatement stmt = conn.prepareStatement(r.getQuery());
            stmt.setInt(1, subId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString(1);
                return "EMAIL:" + (email == null ? "" : email.trim());
            }
            return "EMAIL:";

        } catch (SQLException e) {
            e.printStackTrace();
            return "EMAIL_ERROR:" + e.getMessage();
        }
    }

    /* ================= LOGIN ================= */
    private String checkLogin(Request r) {
        try {
            PreparedStatement stmt = conn.prepareStatement(r.getQuery());
            stmt.setInt(1, ((LoginRequest) r).getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getBoolean(1)) return "User found";
            return "User not found";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Login error";
        }
    }

    /* ================= REGISTER ================= */
    private String addNewUser(Request r) {
        Subscriber user = ((RegisterRequest) r).getUser();

        try {
            PreparedStatement stmt = conn.prepareStatement(r.getQuery());
            stmt.setString(1, user.getFirstName() + " " + user.getLastName());
            stmt.setInt(2, user.getSubscriberID());
            stmt.setString(3, user.getUserName());
            stmt.setString(4, user.getPhone());
            stmt.setString(5, user.getEmail());
            stmt.executeUpdate();
            return "✅ User registered successfully.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Registration failed.";
        }
    }
}
