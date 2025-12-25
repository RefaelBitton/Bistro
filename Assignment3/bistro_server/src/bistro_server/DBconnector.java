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
import entities.ShowTakenSlotsRequest;
import entities.Subscriber;
import entities.Table;
import entities.WriteRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A class that handles all operations on the database, receiving requests and handling them 
 * */
public class DBconnector {
	/**The connection to the Database*/
    private Connection conn;
    
    
    /**
     * Constructor, initiating the connection and fields
     * */
    public DBconnector(){
        try //connect DB
        {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeeded");
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }


    }


    public String addOrder(Order o,String query) {
       
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

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
            stmt.setString(8, "OPEN");

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
    public String checkSlot(Request r) {
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
    public String getTakenSlots(Request r) {
    	ShowTakenSlotsRequest req = (ShowTakenSlotsRequest) r;
    	LocalDateTime from = LocalDateTime.parse(req.getOrderDateTime()).minusHours(1).minusMinutes(30);
    	LocalDateTime to = LocalDateTime.parse(req.getOrderDateTime()).plusHours(1).plusMinutes(30);
        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) sb.append(rs.getString(1)).append(",");
                return sb.toString();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }

    }
    public String OrderNumber() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT IFNULL(MAX(order_number), 0) + 1 AS next_num FROM `order`");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getString(1);
            

        } catch (SQLException e) {
            e.printStackTrace();
            return "ERROR:" + e.getMessage();
        }
		return "";
    }


    /* ================= READ ORDER =================
       Make sure your ReadRequest SELECT uses order_datetime as the 2nd column.
    */
    public String getOrder(Request r) {
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
    public String readEmail(String subId) {

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT email FROM `user` WHERE subscriber_id = ?");
            stmt.setInt(1, Integer.parseInt(subId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString(1);
                return email;
            }
            return "";

        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }


   
	/**
	 * 
	 * @param r A LoginRequest
	 * @return The resulting string, a message or the subscriber if found
	 */
	public String checkLogin(Request r) {
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
	public String addNewUser(Request r) {
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
	
	public String cancelOrder(Request r) {
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

	public List<Table> getAllTables() {
		ArrayList<Table> tables = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bistro.table_info");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("table_number");
				int capacity = rs.getInt("number_of_seats");
				tables.add(new Table(id, capacity, false));
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
