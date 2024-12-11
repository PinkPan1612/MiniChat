package server;

import java.io.*;
import java.net.Socket;
import java.util.List;

import data.GetAllUserProfileList;
import data.UserProfile;

public class ThreadNhapXuat extends Thread {
    private Socket socket;
    private int userID;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String tenNguoiDung;
    private String matKhau;

    public ThreadNhapXuat(Socket socket) {
        this.socket = socket;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }
 

    @Override
    public void run() {
        GetAllUserProfileList quanLyCSDL = new GetAllUserProfileList();
        try {
            // Tạo kênh nhập xuất
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) { // Vòng lặp chờ yêu cầu từ client
                String thongTinDangNhap = nhan();
                if (thongTinDangNhap != null) {

                    String[] thongTin = thongTinDangNhap.split("#~");
                    if (thongTin[0].equals("dangNhap")) {
                        tenNguoiDung = thongTin[1];
                        matKhau = thongTin[2];

                        // Kiểm tra thông tin người dùng và thông báo đăng nhập thành công
                        if (quanLyCSDL.validateUser(tenNguoiDung, matKhau)) {
                            gui("dangNhapThanhCong#~" + tenNguoiDung + "#~success#~" + matKhau 
                                + "#~" + Server.xuLy.getEmail(tenNguoiDung) 
                                + "#~" + Server.xuLy.getFullName(tenNguoiDung)
                                + "#~" + Server.xuLy.getGender(tenNguoiDung)
                            );

                            // Cập nhật trạng thái người dùng online và thoát khỏi vòng lặp đăng nhập
                            Server.xuLy.guiUserNameChoClient(tenNguoiDung);
                            Server.xuLy.guiDanhSachUserDangOnline();
                            Server.xuLy.guiMoiNguoi("capNhatDangNhapDangXuat#~server#~***username " 
                            + Server.xuLy.getFullName(tenNguoiDung) + " đã đăng nhập***");

                            // Nhận và chuyển tiếp tin nhắn từ người dùng
                            String thongDiep;
                            while ((thongDiep = nhan()) != null) {
                                Server.xuLy.chuyenTiepThongDiep(thongDiep, tenNguoiDung);
                            }
                            break; // Thoát khỏi vòng lặp khi người dùng đăng nhập thành công
                        } else {
                            gui("dangNhapThatBai#~Sai tên đăng nhập hoặc mật khẩu"); // Giữ kết nối để client thử lại
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi kết nối với người dùng: " + e.getMessage());
        } finally {
            dongKetNoi(); // Đóng kết nối chỉ khi thực sự cần thiết
        }
    }

    // Nhận dữ liệu từ client
    public String nhan() throws IOException {
        return bufferedReader.readLine();
    }

    // Gửi dữ liệu đến client
    public void gui(String thongDiep) throws IOException {
        bufferedWriter.write(thongDiep);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    // Đóng kết nối khi cần thiết
    private void dongKetNoi() {
        try {
            if (tenNguoiDung != null) {
                // Xóa người dùng khỏi danh sách kết nối
                Server.xuLy.loaiRa(tenNguoiDung); 

                // Gửi thông báo cập nhật trạng thái đăng xuất
                Server.xuLy.guiMoiNguoi("capNhatDangNhapDangXuat#~server#~***username " + tenNguoiDung + " đã đăng xuất***");

                // Gửi lại danh sách online hiện tại sau khi user đăng xuất
                Server.xuLy.guiDanhSachUserDangOnline();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close(); // Đảm bảo chỉ đóng socket khi thực sự cần thiết
            }
            System.out.println("Đã đóng kết nối với " + tenNguoiDung);
        } catch (IOException e) {
            System.out.println("Không thể đóng kết nối: " + e.getMessage());
        }
    }
}
