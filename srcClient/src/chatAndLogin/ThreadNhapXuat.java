package chatAndLogin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ThreadNhapXuat extends Thread {
	//Thuộc tính của lớp
	private String serverName;
	private int port;
	
	Label labelBan, labelFullName, labelEmail, lbgender;
	TextArea textAreaTrucTuyen;
	TextArea textAreaNoiDung;
	TextField textFieldSoanThao;
	ComboBox<String> comboBoxChonNguoiNhan;
	
	//Biến cục bộ
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private TextField txtuserName;
	private PasswordField txtpassW;
	private String userName;
	private String fullName;
	private String email;
	private String passW;
	private String gender;
	
	//Contructor
	public ThreadNhapXuat(String serverName, int port, Label labelBan, TextArea textAreaTrucTuyen,
			TextArea textAreaNoiDung, TextField textFieldSoanThao, ComboBox<String> comboBoxChonNguoiNhan, 
			TextField userName, PasswordField passW, Label lbFullName, Label lbEmail, Label lbgender ) {
		this.serverName = serverName;
		this.port = port;
		this.labelBan = labelBan;
		this.textAreaTrucTuyen = textAreaTrucTuyen;
		this.textAreaNoiDung = textAreaNoiDung;
		this.textFieldSoanThao = textFieldSoanThao;
		this.comboBoxChonNguoiNhan = comboBoxChonNguoiNhan;
		this.txtuserName = userName;
		this.txtpassW = passW;
		this.labelFullName = lbFullName;
		this.labelEmail = lbEmail;
		this.lbgender = lbgender;
	}
	

	@Override
	public void run() {
		try {
			socket = new Socket(serverName,port);
			System.out.println("Kết quả thành công!");
			
			//Mở kênh nhập dữ liệu
			InputStream in = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			bufferedReader = new BufferedReader(reader);
			
			//Mở kênh xuất dữ liệu
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(out);
			bufferedWriter = new BufferedWriter(writer);
			
			System.out.println("Máy trạm: Mở kênh nhập xuất dữ liệu với máy chủ thành công!");
			
			//Liên tục nhập dữ liệu từ kênh, sau đó phân tích cú pháp của thông
			//điệp để hiển thị lên client
			
			nhan();
		
		} catch (IOException e) {
			System.out.println("Lỗi rồi: " + e.getMessage());
			return;
		}finally {
	        try {
	            if (socket != null && !socket.isClosed()) {
	                socket.close();
	            }
	        } catch (IOException ex) {
	            System.out.println("Không thể đóng socket: " + ex.getMessage());
	        }
	    }
	}
	
//	Nhấn thông điệp từ kênh, sau đó phân tích cú pháp của thông điệp
//	để hiển thị lên màn hình của máy trạm
	
	public void nhan() {
	    String thongDiepNhan;
	    while (true) {
	        try {
	            thongDiepNhan = bufferedReader.readLine();
	            if (thongDiepNhan != null) {
	                String[] phanTachThongDiep = thongDiepNhan.split("#~");

	                // Phân tích "Loại" thông điệp để hiển thị lên client đúng vị trí
	                // Hiển thị userName lên labelBan
	                // Phân tích "Loại" thông điệp để hiển thị lên client đúng vị trí
	                if (phanTachThongDiep[0].equals("dangNhapThanhCong")) {
	                	Platform.runLater(() -> thongBao("Đăng nhập thành công!"));
	                    this.userName = phanTachThongDiep[1];
	                    this.passW = phanTachThongDiep[3];
	                    this.email = phanTachThongDiep[4];
	                    this.fullName = phanTachThongDiep[5];
	                    this.gender = phanTachThongDiep[6];
	                    Platform.runLater(() -> {labelBan.setText("Tên đăng nhập: " + userName);
	                    						labelEmail.setText("Email: " + email);
	                    						labelFullName.setText("Tên: " + fullName);
	                    						lbgender.setText("Giới tính: " + gender);
	                    						});                 
	                }
	                else if (phanTachThongDiep[0].equals("dangNhapThatBai")) {
	                    Platform.runLater(() -> thongBao("Đăng nhập thất bại. Bạn đã nhập sai tên đăng nhập hoặc mật khẩu!"));
	                  
	                }
	                // cập nhật danh sách đang online	
	                 else if (phanTachThongDiep[0].equals("capNhatDSOnline")) {
	                    capNhatDSOnline(phanTachThongDiep[2]);

	                // Hiển thị ra màn hình 1 userName đăng nhập hoặc đăng xuất
	                } else if (phanTachThongDiep[0].equals("capNhatDangNhapDangXuat")) {
	                    textAreaNoiDung.setText(textAreaNoiDung.getText() + phanTachThongDiep[2] + "\n");

	                // Hiển thị nội dung tin nhắn của một người gửi đến cho riêng mình
	                } else if (phanTachThongDiep[0].equals("guiMotNguoi")) {
	                     textAreaNoiDung.setText(textAreaNoiDung.getText() + "user " 
	                    		 + phanTachThongDiep[1] + ": " 
	                    		 + phanTachThongDiep[2] + "\n")
	                    ;
	                }

	                // Hiển thị nội dung tin nhắn của một người gửi đến cho nhiều người
	                else if (phanTachThongDiep[0].equals("guiMoiNguoi")) {
	                    
	                    textAreaNoiDung.setText(textAreaNoiDung.getText() + "user " 
	                    		+ phanTachThongDiep[1] + " (gửi mọi người): " 
	                    		+ phanTachThongDiep[2] + "\n");
	                }
	            }

	        } catch (IOException e) {
	            System.out.println("Lỗi rồi: " + e.getMessage());
	            return;
	        }
	    }
	}
	
//	Cập nhật danh sách đang trực tuyến. đồng thời cập nhật ComboBox chọn người nhận
//	@param danhSachOnline Danh sách trực tuyến do server gửi đến, phân cách nhau bằng dấu
	
	public void capNhatDSOnline(String danhSachOnline) {
		String[] tachDanhSachOnline = danhSachOnline.split("-");
		String usernameOnline = "";
		List<String> dsOnline = new ArrayList<>();
		
		for (String i : tachDanhSachOnline) {
			dsOnline.add(i);
			usernameOnline += "user: " + i + "\n";
		}
		textAreaTrucTuyen.setText(usernameOnline);
		
		//cập nhật danh sách vào ComboBox, trừ chính mình
		capNhatComboBoxChonNguoiNhan(dsOnline);
	}

	public void capNhatComboBoxChonNguoiNhan(List<String> onlineList) {
	    // Use Platform.runLater to ensure UI updates are on the main thread
	    Platform.runLater(() -> {
	        comboBoxChonNguoiNhan.getItems().clear();
	        comboBoxChonNguoiNhan.setPromptText("Chọn người nhận");

	        // Add "Mọi người" for broadcast
	        comboBoxChonNguoiNhan.getItems().add("Mọi người");

	        // Add each fullName to the ComboBox, skipping the current user's fullName
	        onlineList.stream()
	                .filter(user -> !user.equals(this.fullName)) // Exclude current user's fullName
	                .forEach(user -> comboBoxChonNguoiNhan.getItems().add(user)); // Display as fullName
	    });
	}

	public void gui() {
	    String thongDiep = textFieldSoanThao.getText().trim();
	    String diaChiDich = comboBoxChonNguoiNhan.getValue();

	    // Kiểm tra nếu người dùng chưa nhập tin nhắn hoặc chưa chọn người nhận
	    if (thongDiep.isEmpty() || diaChiDich == null) {
	        thongBao("Bạn chưa nhập thông điệp hoặc chưa chọn người nhận");
	    } else {
	        // Kiểm tra gửi cho tất cả mọi người
	        if (diaChiDich.equals("Mọi người")) {
	            // Gửi tin nhắn cho mọi người
	            xuat("guiMoiNguoi#~" + userName + "#~" + thongDiep);

	            // Cập nhật nội dung tin nhắn cho chính mình
	            Platform.runLater(() -> {
	                textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn (gửi mọi người): " + thongDiep + "\n");
	            });
	        } else {
	            // Gửi cho một người
	            
	            xuat("guiMotNguoi#~" + userName + "#~" + thongDiep + "#~" + diaChiDich);

	            // Cập nhật nội dung tin nhắn cho chính mình
	            Platform.runLater(() -> {
	                textAreaNoiDung.setText(textAreaNoiDung.getText() + "Bạn (gửi username" + diaChiDich + "): " + thongDiep + "\n");
	            });
	        }
	    }
	    textFieldSoanThao.setText("");  // Xóa tin nhắn sau khi gửi
	}

	public void xuat(String thongDiep) {
	    try {
	        bufferedWriter.write(thongDiep);
	        bufferedWriter.newLine();
	        bufferedWriter.flush();
	    } catch (IOException e) {
	        System.out.println("Lỗi rồi: " + e.getMessage());
	    }
	}

	public void thongBao(String tb) {
	    Platform.runLater(() -> {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Thông Báo");
	        alert.setHeaderText(null);
	        alert.setContentText(tb);
	        alert.showAndWait();
	    });
	}

	public void dangNhap() {
	    String userNameString = txtuserName.getText().trim();
	    String passWString = txtpassW.getText().trim();

	    if (userNameString.isEmpty() || passWString.isEmpty()) {
	        thongBao("Bạn chưa nhập tên đăng nhập hoặc chưa nhập mật khẩu");
	        return;
	    }

	    try {
	    	// gửi yêu cầu đến server để đăng nhập
	        xuat("dangNhap#~" + userNameString + "#~" + passWString);
	        
	    } catch (Exception e) {
	        System.out.println("Lỗi đăng nhập: " + e.getMessage());
	        thongBao("Đăng nhập thất bại do lỗi kết nối.");
	    }
	}


}
