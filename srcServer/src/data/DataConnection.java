package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
	
	private static final String URL = "jdbc:mysql://localhost:3306/ChatApp";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "Huybguyen881@";
	
	 // Phương thức để lấy kết nối
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Đăng ký driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kết nối đến database
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối thành công đến MySQL Database!");
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver MySQL JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Kết nối không thành công.");
            e.printStackTrace();
        }
        return connection;
    }
}

