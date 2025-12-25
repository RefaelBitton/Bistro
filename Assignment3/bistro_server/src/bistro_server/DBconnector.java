package bistro_server;

import java.sql.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import entities.CancelRequest;
import entities.CheckSlotRequest;
import entities.LoginRequest;
import entities.Order;
import entities.ReadEmailRequest;
import entities.ReadRequest;
import entities.RegisterRequest;
import entities.Request;
import entities.RequestHandler;
import entities.RequestType;
import entities.ShowOpenSlotsRequest;
import entities.Subscriber;
import entities.WriteRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * A class that handles all operations on the database, receiving requests and handling them 
 * */
public class DBconnector {
	/**The connection to the Database*/
    private Connection conn;
    /**formatter for parsing dates*/
    private HashMap<RequestType,RequestHandler> handlers; //managing requests by their Types
    
    
    /**
     * Constructor, initiating the connection and fields
     * */
    public DBconnector(){
        try //connect DB
        {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "shonv2014!");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
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
        handlers.put(RequestType.CANCEL_REQUEST, this::cancelOrder);
        handlers.put(RequestType.CHECK_SLOT, this::checkSlot);
        handlers.put(RequestType.SHOW_OPEN_SLOTS, this::getTakenSlots);
        handlers.put(RequestType.ORDER_NUMBER, this::OrderNumber);

    }

    public String handleQueries(Object obj) {
        Request r = (Request) obj;
        RequestHandler handler = handlers.get(r.getType());
        if (handler == null) return "❌ No handler for request type: " + r.getType();
        return handler.handle(r);
    }

    private String addOrder(Request r) {
        Order o = ((WriteRequest) r).getOrder();

        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {

            stmt.setInt(1, Integer.parseInt(o.getOrderNumber()));
            stmt.setTimestamp(2, Timestamp.valueOf(o.getOrderDateTime()));
            stmt.setInt(3, Integer.parseInt(o.getNumberOfGuests()));
            stmt.setInt(4, Integer.parseInt(o.getConfirmationCode()));

            int subId = 0;
            try { subId = Integer.parseInt(o.getSubscriberId()); } catch (Exception ignored) {}
            if (subId == 0) stmt.setNull(5, Types.INTEGER);
            else stmt.setInt(5, subId);

            stmt.setDate(6, Date.valueOf(o.getDateOfPlacingOrder()));
            stmt.setString(7, o.getContact());

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                return "✅ Order saved successfully!\nOrder Number: " + o.getOrderNumber() +
                       "\nConfirmation Code: " + o.getConfirmationCode();
            }
            return "❌ Order was not saved.";

        } catch (SQLIntegrityConstraintViolationException e) {
            return "❌ This date and time is already taken. Please choose another time.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            //return "❌ Error saving order.";
            return "❌ ERROR: " + e.getClass().getName() + " | " + e.getMessage();
        }
    }
    private String checkSlot(Request r) {
        CheckSlotRequest req = (CheckSlotRequest) r;

        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {
            stmt.setTimestamp(1, Timestamp.valueOf(req.getOrderDateTime()));
    
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? "TAKEN" : "FREE";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }
    }
    private String getTakenSlots(Request r) {
        ShowOpenSlotsRequest req = (ShowOpenSlotsRequest) r;

        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {
            stmt.setTimestamp(1, Timestamp.valueOf(req.getFrom()));
            stmt.setTimestamp(2, Timestamp.valueOf(req.getTo()));

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) sb.append(rs.getString(1)).append("\n");
                return sb.toString();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }

    }
    private String OrderNumber(Request r) {
        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery());
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return "NEXT_ORDER_NUMBER:" + rs.getInt(1);
            return "NEXT_ORDER_NUMBER:1";

        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
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


   
	/**
	 * update number of guests in DB
	 * @param r An UpdateRequest to handle
	 * @return A message to the user
	 */
//	private String updateNumOfGuests(Request r) { 
//		String query = r.getQuery();
//		String orderNum = ((UpdateRequest)r).getOrderNum();
//		int numberOfGuests = ((UpdateRequest)r).getNumberOfGuests();
//		int rowsUpdated = 0;
//    	try {
//    		PreparedStatement stmt = conn.prepareStatement(query);
//			stmt.setInt(1, numberOfGuests);
//			stmt.setInt(2, Integer.parseInt(orderNum));
//			rowsUpdated = stmt.executeUpdate();			
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return "";
//		}
//    	if(rowsUpdated == 0) return "an order with that number does not exist.";
//    	return "Updating order " + orderNum + " to " + numberOfGuests + " guests";
//	}
//	/**
//	 * update order's date in DB
//	 * @param r an Update request for the date 
//	 * @return A message to the user
//	 */
//	private String updateDate(Request r) {
//		String query = r.getQuery();
//		String orderNum = ((UpdateRequest)r).getOrderNum();
//		String date = ((UpdateRequest)r).getDate();
//		LocalDate orderDate = LocalDate.parse(date,formatter);
//		int rowsUpdated = 0;
//    	try {
//    		PreparedStatement stmt=conn.prepareStatement(query);
//			stmt.setDate(1, Date.valueOf(orderDate));
//			stmt.setString(2, orderNum);
//			rowsUpdated = stmt.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return "";
//		}
//    	//input check
//    	if(rowsUpdated == 0) return "an order with that number does not exist.";
//    	return "Updating order " + orderNum + " to " + date;
//	}
	/**
	 * 
	 * @param r A LoginRequest
	 * @return The resulting string, a message or the subscriber if found
	 */
	private String checkLogin(Request r) {
		String query = r.getQuery();
		int subcriberId = ((LoginRequest)r).getId();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, subcriberId);
			ResultSet rs =stmt.executeQuery();
			if(rs.next()) {
				String res = "";
				for (int i = 1; i <= 4; i++) {
					res+=rs.getString(i)+",";				
				}
				res+=rs.getString(5);
				return res;
			}
			else{
				return "Not found";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return "";
		}
		
		
		
	/**
	 * 
	 * @param r a RegisterRequest to handle
	 * @return The resulting string, message to the user
	 */
	private String addNewUser(Request r) {
		String query = r.getQuery();
		Subscriber user = ((RegisterRequest)r).getUser();
		System.out.println("In add new user");
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getFirstName()+" "+user.getLastName());
			stmt.setInt(2,user.getSubscriberID());
			stmt.setString(3,user.getUserName());
			stmt.setString(4, user.getPhone());
			stmt.setString(5, user.getEmail());
			if(stmt.executeUpdate()==0) {
				return "ERROR: Couldn't add the user, please try again";
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return "ERROR: Couldn't add the user, please try again"; 
		}
		return "New user added successfully, please keep your ID handy for further login attempts\nUser is:\n"+user;
		
	}
	
	private String cancelOrder(Request r) {
		String query = r.getQuery();
		String orderNum = ((CancelRequest)r).getOrderNum();
		int rowsDeleted = 0;
		try {
    		PreparedStatement stmt = conn.prepareStatement(query);
    		stmt.setString(1, orderNum);
    		rowsDeleted = stmt.executeUpdate();
    		if(rowsDeleted > 0)
    			return "order deleted";
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "order did not deleted";
	}
}
