package bistro_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Order;
import entities.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DBconnector {
    private Connection conn;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DBconnector()
    {

        try 
        {
        	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bistro", "root", "");
        	//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeeded");
         } catch (SQLException ex) 
             {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(1);
            }
       }

    public void handleQuerries(Object obj)
    {
    	Request r = (Request)obj;
    	Order o = r.getOrder();
        PreparedStatement stmt;
        String toUpdate = r.getQuery();

        int orderNumber = Integer.parseInt(o.getOrderNumber());
        LocalDate orderDate = LocalDate.parse(o.getOrderDate(),formatter);
        int numberOfGuests = Integer.parseInt(o.getNumberOfGuests());
        int confirmationCode = Integer.parseInt(o.getConfirmationCode());
        int subscriberID = Integer.parseInt(o.getSubscriberId());
        LocalDate placingOrderDate = LocalDate.parse(o.getDateOfPlacingOrder(),formatter);
        
        try {
            stmt = conn.prepareStatement(toUpdate);
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