package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllUserProfileList {

    private List<UserProfile> userProfileList = new ArrayList<>();

    // Khởi tạo và tải toàn bộ dữ liệu người dùng từ cơ sở dữ liệu
    public GetAllUserProfileList() {
        loadUserProfileFromDatabase();
    }

    // Tải dữ liệu người dùng từ cơ sở dữ liệu
    public void loadUserProfileFromDatabase() {
        String query = "SELECT * FROM users"; 

        try (Connection connection = DataConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Lặp qua các dòng kết quả và thêm vào danh sách
            while (resultSet.next()) {
                int userID = resultSet.getInt("id");
                String userN = resultSet.getString("userName");
                String passW = resultSet.getString("passW");
                String fullName = resultSet.getString("fullName");
                String email = resultSet.getString("email");
                String avatar = resultSet.getString("avatar");
                String gender = resultSet.getString("gender");

                // Tạo đối tượng UserProfile và thêm vào danh sách
                userProfileList.add(new UserProfile(userN, fullName, email, gender, passW, userID, avatar));   
            }
            System.out.println("Dữ liệu đã được tải lên từ cơ sở dữ liệu.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi không kết nối được cơ sở dữ liệu");
        }
    }

    // Lấy tất cả các UserProfile trong danh sách đã tải
    public List<UserProfile> getAllUserProfileList() {
        return new ArrayList<>(userProfileList); // Trả về bản sao của danh sách
    }

    // Làm mới danh sách người dùng
    public void refreshInvoiceList() {
        userProfileList.clear(); // Xóa danh sách hiện tại
        loadUserProfileFromDatabase(); // Tải lại dữ liệu từ cơ sở dữ liệu
    }

    // Xác thực người dùng dựa trên tên đăng nhập và mật khẩu
    public boolean validateUser(String userName, String passW) {
        String query = "SELECT * FROM users WHERE userName = ? AND passW = ?";

        try (Connection connection = DataConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, passW);

            // Thực hiện truy vấn và kiểm tra kết quả
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return true; // Người dùng hợp lệ
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Người dùng không hợp lệ
    }
}
