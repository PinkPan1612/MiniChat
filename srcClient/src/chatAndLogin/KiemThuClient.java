package chatAndLogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiemThuClient extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load giao diện từ tệp FXML đã thiết kế
            Parent root = FXMLLoader.load(getClass().getResource("ChatandLogin.fxml"));
            
            // Tạo cảnh và gán vào sân khấu chính
            Scene scene = new Scene(root);
            primaryStage.setTitle("Ứng dụng Chat Client");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức main để chạy ứng dụng JavaFX
    public static void main(String[] args) {
        launch(args);  // Gọi hàm launch() của JavaFX để khởi chạy ứng dụng
    }
}