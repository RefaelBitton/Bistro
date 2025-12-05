package bistro_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import entities.Order;
import entities.ReadRequest;
import entities.Request;
import entities.RequestHandler;
import entities.RequestType;
import entities.UpdateRequest;
import entities.WriteRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class DBconnector {
    private Connection conn;
    private DateTimeFormatter formatter;
    private HashMap<RequestType,RequestHandler> handlers;
    public DBconnector(){
    	formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try 
        {
        	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "123456789");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeeded");
         } catch (SQLException ex) 
             {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(1);
            }
        handlers = new HashMap<>();
        handlers.put(RequestType.WRITE_ORDER, this::addOrder);
        handlers.put(RequestType.READ_ORDER, this::getOrder);
        handlers.put(RequestType.UPDATE_GUESTS, this::updateNumOfGuests);
        handlers.put(RequestType.UPDATE_DATE, this::updateDate);
       }
    

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
	
	private String updateNumOfGuests(Request r) {
		String query = r.getQuery();
		String orderNum = ((UpdateRequest)r).getOrderNum();
		int numberOfGuests = ((UpdateRequest)r).getNumberOfGuests();
    	try {
    		PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, numberOfGuests);
			stmt.setInt(2, Integer.parseInt(orderNum));
			stmt.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
    	return "Updating order " + orderNum + " to " + numberOfGuests + " guests";
	}
	
	private String updateDate(Request r) {
		String query = r.getQuery();
		String orderNum = ((UpdateRequest)r).getOrderNum();
		String date = ((UpdateRequest)r).getDate();
		LocalDate orderDate = LocalDate.parse(date,formatter);
    	try {
    		PreparedStatement stmt=conn.prepareStatement(query);
			stmt.setDate(1, Date.valueOf(orderDate));
			stmt.setString(2, orderNum);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
    	return "Updating order " + orderNum + " to " + date;
	}
}