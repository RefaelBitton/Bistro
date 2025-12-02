package bistro_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Order;
import entities.Request;
import entities.RequestType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DBconnector {
    private Connection conn;
    private DateTimeFormatter formatter;

    public DBconnector()
    {
    	formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try 
        {
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "");
        	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeeded");
         } catch (SQLException ex) 
             {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(1);
            }
       }

    public String handleQueries(Object obj)
    {
    	Request r = (Request)obj;
    	Order o = r.getOrder();
        String toUpdate = r.getQuery();
        if (r.getType()==RequestType.WRITE) handleNewOrder(o,toUpdate);
        else if(r.getType()==RequestType.READ) return handleOrderSearch(o,toUpdate);
        return "";
    }
    
    //TODO: change result to StringBuilder
    private String handleOrderSearch(Order o,String query) {
    	String result = "Results:\n";
    	try {
    		PreparedStatement stmt=conn.prepareStatement(query);
			stmt.setString(1, o.getOrderNumber());
			ResultSet rs = stmt.executeQuery();
			int i = 1;
			while(rs.next())
	 		{
				 result+=i + ". \n";
				 result+= "Order Number: " + rs.getString(1) + "\n";
				 result+= "Order Date: " + rs.getString(2) + "\n";
				 result+= "Number Of Guests: " + rs.getString(3) + "\n";
				 result+= "Confirmation Code: " + rs.getString(4) + "\n";
				 result+= "Subscriber ID: " + rs.getString(5) + "\n";
				 result+= "Date of placing order: " + rs.getString(6) + "\n";
				 i++;
			} 
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    	
 		
	}

	private void handleNewOrder(Order o,String query) {
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

        } catch (SQLException e) {    e.printStackTrace();}    	
    }
}