package bistro_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


public class DBconnector {
    private Connection conn;

    public void connectToDB()
    {

        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?allowLoadLocalInfile=true&serverTimezone=Asia/Jerusalem&useSSL=false", "root", "Hodvak123!");
            System.out.println("SQL connection succeed");
         } catch (SQLException ex) 
             {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
       }

    @SuppressWarnings("unchecked")
    public void handleQuerries(Object obj)
    {
        ArrayList<String> arr = new ArrayList<>();
        arr = (ArrayList<String>)obj;

        PreparedStatement stmt;
        String toUpdate = "INSERT INTO order (order_number, order_date, number_of_guests, confirmation_code, subscriber_id, date_of_placing_order) VALUES(?, ?, ?, ?, ?, ?);";

        int orderNumber = Integer.parseInt(arr.get(0));
        LocalDate orderDate = LocalDate.parse("28/11/2025");
        int numberOfGuests = Integer.parseInt(arr.get(2));
        int confirmationCode = Integer.parseInt(arr.get(3));
        int subscriberID = Integer.parseInt(arr.get(4));
        LocalDate placingOrderDate = LocalDate.parse("28/11/2025");

        try {
            stmt = conn.prepareStatement(toUpdate);
            stmt.setInt(1, orderNumber);
            stmt.setDate(2, orderDate);
            stmt.setInt(3, numberOfGuests);
            stmt.setInt(4, confirmationCode);
            stmt.setInt(5, subscriberID);
            stmt.setDate(6, placingOrderDate);
            stmt.executeUpdate();

        } catch (SQLException e) {    e.printStackTrace();}
    }
}