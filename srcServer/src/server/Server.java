package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import data.GetAllUserProfileList;
import data.UserProfile;

public class Server {
    // Biến toàn cục
    public static volatile XuLy xuLy;

    // biến cục bộ
    private ServerSocket serverSocket = null;
    

    // khởi dựng
    public Server(int port) throws IOException {
        try {
            this.serverSocket = new ServerSocket(port); // port > 1023
            xuLy = new XuLy();
            
        } catch (Exception e) {
            System.out.println("Lỗi rồi (1): " + e.getMessage());
        }
    }

    public void chay() {
    	
        Socket incomingSocket;
        try {
            while (true) {
                System.out.println("Máy chủ: đang chờ máy trạm đăng nhập ...");
             // Kiểm tra xem serverSocket có phải là null hay không 
                if (serverSocket == null) { throw new IOException("ServerSocket is null."); }

                // Chấp nhận yêu cầu kết nối từ client
                incomingSocket = serverSocket.accept();
                System.out.println("Máy chủ: Đã có máy trạm " +
                        incomingSocket.getRemoteSocketAddress() +
                        " kết nối vào máy chủ " + incomingSocket.getLocalSocketAddress());

                // Tạo Thread Nhập xuất để quản lý từng Client
                ThreadNhapXuat threadNhapXuat = new ThreadNhapXuat(incomingSocket);
                threadNhapXuat.start();

                // Thêm Thread Nhập- Xuất của từng client vào lớp xử lý để quản lý theo List
                xuLy.themVao(threadNhapXuat);
                System.out.println("Máy chủ: Số Thread đang chạy là: " + xuLy.getKichThuoc());
            }
        } catch (Exception e) {
            System.out.println("Lỗi rồi (2): " + e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Lỗi rồi (3): " + e.getMessage());
            }
        }
    }
}