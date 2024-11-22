package com.BusReservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbConnection {

    private Connection conn;
    private static int count=1;

    public DbConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
            String userName = "busbook";
            String password = "password";
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection!");
        }
    }

    public String execute(String username, String password) {
        String jdbc_template = "INSERT INTO users (id,user_name, password) VALUES (?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(jdbc_template)) {
            pst.setInt(1, ++count);  

            pst.setString(2, username);  
            pst.setString(3, password); 
            int rowsAffected = pst.executeUpdate();
            Boolean finals=rowsAffected > 0;
            return finals.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "false"; // In case of an error, return false
        }
    }
}
