package bistro_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entities.CancelRequest;
import entities.CheckConfCodeRequest;
import entities.LeaveTableRequest;
import entities.LoginRequest;
import entities.Order;
import entities.ReadRequest;
import entities.RegisterRequest;
import entities.Request;
import entities.ShowTakenSlotsRequest;
import entities.Subscriber;
import entities.Table;

/**
 * A class that handles all operations on the database, receiving requests and handling them 
 * */
public class DBconnector {
	/**The connection to the Database*/
    private Connection conn;
    
    DateTimeFormatter f;
 
    /**
     * Constructor, initiating the connection and fields
     * */
    public DBconnector(){
        try //connect DB
        {

			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");


            System.out.println("SQL connection succeeded");
            f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }


    }
    public  String checkConfCode(Request r) {
    	CheckConfCodeRequest req = (CheckConfCodeRequest) r;
    	String res="";
    	try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {
    		stmt.setString(1, req.getcontact());
    		stmt.setTimestamp(2, Timestamp.valueOf(BistroServer.dateTime));
    		stmt.setTimestamp(3, Timestamp.valueOf(BistroServer.dateTime));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				res+=rs.getString(1)+"\n";
			} 
			if(res.equals("")) {
				return "no confiramtion codes found for your contact";
			}
			else {
				ServerUI.updateInScreen("your relevent confiramtion codes for this contact are:\n"+res);
			}
			return "potential confiramtion codes has been sent to your contact";
		} catch (SQLException e) {
			e.printStackTrace();
			return "ERROR:" + e.getMessage();
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
            	System.out.println("Order added to DB: " + o.getOrderNumber());
                return "✅ Order saved successfully!\nOrder Number: " + o.getOrderNumber() +
                       "\nConfirmation Code: " + o.getConfirmationCode();
            }
            return "❌ Order was not saved.";

        } catch (SQLException e) {
            e.printStackTrace();
            return "❌ Database error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            //return "❌ Error saving order.";
            return "❌ ERROR: " + e.getClass().getName() + " | " + e.getMessage();
        }
    }
    
    

	
 public String getTakenSlots(Request r) {
    	ShowTakenSlotsRequest req = (ShowTakenSlotsRequest) r;
    	LocalDateTime from = req.getFrom();
    	LocalDateTime to = req.getTo();
        try (PreparedStatement stmt = conn.prepareStatement(r.getQuery())) {
            stmt.setTimestamp(1, Timestamp.valueOf(from));
            stmt.setTimestamp(2, Timestamp.valueOf(to));

            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) sb.append(rs.getString(1)).append(":").append(rs.getString(2)).append(",");
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
				for (int i = 1; i <= 5; i++) {
					res+=rs.getString(i)+",";				
				}
				res+=rs.getString(6);
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
		String code = ((CancelRequest)r).getCode();
		int rowsDeleted = 0;
		try {
    		PreparedStatement stmt = conn.prepareStatement(query);
    		stmt.setString(1, orderNum);
    		stmt.setString(2, code);
    		rowsDeleted = stmt.executeUpdate();
    		if(rowsDeleted > 0)
    			return "order deleted";
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return "order was not deleted";
	}

	public List<Table> getAllTables() {
		ArrayList<Table> tables = new ArrayList<>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `table`;");
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
	
	public String updateDetails(Request r) {
		String query = r.getQuery();
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			int rowsUpdated = stmt.executeUpdate();
			if(rowsUpdated > 0)
				return "Details updated successfully.";
			else
				return "No details were updated.";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error updating details: " + e.getMessage();
		}
	}
	
	public String getOrderHistory(Request r) {
		String query = r.getQuery();
		String result = "";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) return "No order history found.";

			do {
				result += "Order Number: " + rs.getString("order_number") + ", ";
				result += "Order DateTime: " + rs.getString("order_datetime") + ", ";
				result += "Guests: " + rs.getString("number_of_guests") + ", ";
				result += "Confirmation Code: " + rs.getString("confirmation_code") + ", ";
				result += "Placed On: " + rs.getString("date_of_placing_order") + ", ";
				result += "Status: " + rs.getString("status") + "\n";
			} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			return "❌ Error retrieving order history.";
		}

		return result;
	}
	public String getAllActiveOrders(Request r) {
		String query = r.getQuery();
		String result = "";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) return "No active orders found.";

			do {
				result += "Order Number: " + rs.getString("order_number") + ", ";
				result += "Order DateTime: " + rs.getString("order_datetime") + ", ";
				result += "Guests: " + rs.getString("number_of_guests") + ", ";
				result += "Confirmation Code: " + rs.getString("confirmation_code") + ", ";
				result += "Subscriber ID: " + rs.getString("subscriber_id") + ", ";
				result += "Placed On: " + rs.getString("date_of_placing_order") + ", ";
				result += "Contact: " + rs.getString("contact") + ", ";
				result += "Status: " + rs.getString("status") + "\n";
			} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			return "❌ Error retrieving active orders.";
		}
		return result;
	}
	public String getAllSubscribers(Request r) {
		String query = r.getQuery();
		String result = "";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) return "No subscribers found.";

			do {
				result += "Subscriber ID: " + rs.getString("subscriber_id") + ", ";
				result += "Name: " + rs.getString("full_name") + ", ";
				result += "Username: " + rs.getString("username") + ", ";
				result += "Phone: " + rs.getString("phone_number") + ", ";
				result += "Email: " + rs.getString("email") + "\n";
			} while (rs.next());

		} catch (SQLException e) {
			e.printStackTrace();
			return "❌ Error retrieving subscribers.";
		}

		return result;
	}
	public String getOrderFromConfCode(String query, String confCode) {
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, confCode);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("order_number")+","+
					   rs.getString("order_datetime")+","
					   +rs.getString("number_of_guests")+","
					   +rs.getString("confirmation_code")+","
					   +rs.getString("subscriber_id")+","
					   +rs.getString("date_of_placing_order")+","
					   +rs.getString("contact");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "Not found";
	}
	public String closeOrder(LeaveTableRequest r) {
		String query = r.getQuery();
		String confcode = r.getConfCode();
		try {
			PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmt.setInt(1, Integer.parseInt(confcode));
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				rs.updateString("status", "CLOSED");
				rs.updateRow();
				return rs.getString("subscriber_id");
			}
			else {
				return "Not found";
			}
		
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return "Error";
	}
}