package bistro_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import entities.LoginRequest;
import entities.Order;
import entities.ReadRequest;
import entities.RegisterRequest;
import entities.Request;
import entities.RequestHandler;
import entities.RequestType;
import entities.UpdateRequest;
import entities.Subscriber;
import entities.WriteRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * A class that handles all operations on the database, receiving requests and handling them 
 * */
public class DBconnector {
	/**The connection to the Database*/
    private Connection conn;
    /**formatter for parsing dates*/
    private DateTimeFormatter formatter;
    /**A map for handling a request based on it's type,
     *  and routing to the correct method */
    private HashMap<RequestType,RequestHandler> handlers; //managing requests by their Types
    /**
     * Constructor, initiating the connection and fields
     * */
    public DBconnector(){
    	formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try //connect DB
        {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeeded");
         } catch (SQLException ex) 
             {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(1);
            }
        //in the HashMap: keys - requests type, values - functionality of each type
        handlers = new HashMap<>(); 
        handlers.put(RequestType.WRITE_ORDER, this::addOrder);
        handlers.put(RequestType.READ_ORDER, this::getOrder);
        handlers.put(RequestType.UPDATE_GUESTS, this::updateNumOfGuests);
        handlers.put(RequestType.UPDATE_DATE, this::updateDate);
        handlers.put(RequestType.LOGIN_REQUEST, this::checkLogin);
        handlers.put(RequestType.REGISTER_REQUEST, this::addNewUser);
   }
    
    /**A method that routes the request to the correct function based on the value in 'handlers'*/
    public String handleQueries(Object obj)
    {
    	Request r = (Request)obj;
        return handlers.get(r.getType()).handle(r);
    	//if (r.getType()==RequestType.WRITE) return addOrder(r);
        //else if(r.getType()==RequestType.READ) return getOrder(r);
        //else if(r.getType()==RequestType.UPDATEGUESTS) return updateNumOfGuests(r);
        //else if(r.getType()==RequestType.UPDATEDATE) return updateDate(r);
        //return "";
    }
    /**
     * getting details of existing order from DB
     * @param r request to handle (a ReadRequest)
     * @return A message to the user or the order if found
     */
    private String getOrder(Request r) { 
    	String query = r.getQuery();
    	String orderNum = ((ReadRequest)r).getOrderNum();
    	String result = "Results:\n";
		boolean orderFound = false;
    	try { 
    		PreparedStatement stmt=conn.prepareStatement(query);
			stmt.setString(1, orderNum);
			ResultSet rs = stmt.executeQuery();

			while(rs.next())
	 		{
				 result+= "Order Number: " + rs.getString(1) + "\n";
				 result+= "Order Date: " + rs.getString(2) + "\n";
				 result+= "Number Of Guests: " + rs.getString(3) + "\n";
				 result+= "Confirmation Code: " + rs.getString(4) + "\n";
				 result+= "Subscriber ID: " + rs.getString(5) + "\n";
				 result+= "Date of placing order: " + rs.getString(6) + "\n";
				 orderFound = true;
			} 
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return orderFound? result : "No results for that order number";		
	}
    /**
     * adding order to DB
     * @param r A WriteRequest to handle
     * @return The resulting string
     */
	private String addOrder(Request r) {
		String query = r.getQuery();
		Order o = ((WriteRequest)r).getOrder();
    	PreparedStatement stmt;
        int orderNumber = Integer.parseInt(o.getOrderNumber());
        int confirmationCode = Integer.parseInt(o.getConfirmationCode());
        LocalDate orderDate = LocalDate.parse(o.getOrderDate(),formatter);
        int numberOfGuests = Integer.parseInt(o.getNumberOfGuests());
        int subscriberID = Integer.parseInt(o.getSubscriberId());
        LocalDate placingOrderDate = LocalDate.parse(o.getDateOfPlacingOrder(),formatter);
        
        try {
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderNumber);
            stmt.setDate(2, Date.valueOf(orderDate));
            stmt.setInt(3, numberOfGuests);
            stmt.setInt(4, confirmationCode);
            stmt.setInt(5, subscriberID);
            stmt.setDate(6, Date.valueOf(placingOrderDate));
            stmt.executeUpdate();

        }catch (SQLIntegrityConstraintViolationException e1) {
        	return "An order with that number already exists";
        }catch (SQLException e) {    e.printStackTrace();}
        return "";           	
    }
	/**
	 * update number of guests in DB
	 * @param r An UpdateRequest to handle
	 * @return A message to the user
	 */
	private String updateNumOfGuests(Request r) { 
		String query = r.getQuery();
		String orderNum = ((UpdateRequest)r).getOrderNum();
		int numberOfGuests = ((UpdateRequest)r).getNumberOfGuests();
		int rowsUpdated = 0;
    	try {
    		PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, numberOfGuests);
			stmt.setInt(2, Integer.parseInt(orderNum));
			rowsUpdated = stmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
    	if(rowsUpdated == 0) return "an order with that number does not exist.";
    	return "Updating order " + orderNum + " to " + numberOfGuests + " guests";
	}
	/**
	 * update order's date in DB
	 * @param r an Update request for the date 
	 * @return A message to the user
	 */
	private String updateDate(Request r) {
		String query = r.getQuery();
		String orderNum = ((UpdateRequest)r).getOrderNum();
		String date = ((UpdateRequest)r).getDate();
		LocalDate orderDate = LocalDate.parse(date,formatter);
		int rowsUpdated = 0;
    	try {
    		PreparedStatement stmt=conn.prepareStatement(query);
			stmt.setDate(1, Date.valueOf(orderDate));
			stmt.setString(2, orderNum);
			rowsUpdated = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
    	//input check
    	if(rowsUpdated == 0) return "an order with that number does not exist.";
    	return "Updating order " + orderNum + " to " + date;
	}
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
}