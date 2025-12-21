package bistro_server;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import entities.*;

public class DBconnector {

    private Connection conn;
    private DateTimeFormatter formatter;
    private HashMap<RequestType, RequestHandler> handlers;

    public DBconnector() {

        formatter = DateTimeFormatter.ISO_LOCAL_DATE; // FIXED FORMAT

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
        handlers.put(RequestType.LOGIN_REQUEST, this::checkLogin);
        handlers.put(RequestType.REGISTER_REQUEST, this::addNewUser);
    }

    public String handleQueries(Object obj) {
        Request r = (Request) obj;
        return handlers.get(r.getType()).handle(r);
    }
    private String addOrder(Request r) {

        Order o = ((WriteRequest) r).getOrder();

        try {
            PreparedStatement stmt = conn.prepareStatement(r.getQuery());

            stmt.setInt(1, Integer.parseInt(o.getOrderNumber()));
            stmt.setDate(2, Date.valueOf(LocalDate.parse(o.getOrderDate(), formatter)));
            stmt.setInt(3, Integer.parseInt(o.getNumberOfGuests()));
            stmt.setInt(4, Integer.parseInt(o.getConfirmationCode()));
            stmt.setInt(5, Integer.parseInt(o.getSubscriberId()));
            stmt.setDate(6, Date.valueOf(LocalDate.parse(o.getDateOfPlacingOrder(), formatter)));

            int rows = stmt.executeUpdate();

            if (rows == 1) {
                return "✅ Order saved successfully!\nOrder Number: " + o.getOrderNumber();
            } else {
                return "❌ Order was not saved.";
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            return "❌ Order number already exists.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error: " + e.getMessage();
        }
    }

    /* ================= READ ORDER ================= */

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
                result += "Order Date: " + rs.getString(2) + "\n";
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

    /* ================= LOGIN ================= */

    private String checkLogin(Request r) {
        try {
            PreparedStatement stmt = conn.prepareStatement(r.getQuery());
            stmt.setInt(1, ((LoginRequest) r).getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getBoolean(1)) {
                return "User found";
            }
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
