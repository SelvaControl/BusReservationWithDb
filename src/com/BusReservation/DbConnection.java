package com.BusReservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {

    private static int count=1;

    public Connection creatConnection() {
        try {
        	
        	Connection conn = null;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521/xepdb1";
            String userName = "busbooking";
            String password = "password";
            conn = DriverManager.getConnection(url, userName, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish database connection!");
        }
    }


    

    
    
  public void closeConnection(Connection conn) {
    		try {
    			if (conn!= null) {
				conn.close();
    			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	
    }


   
    
    
}
