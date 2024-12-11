package chatAndLogin;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Dieukhienclient implements Initializable {
	//khai báo các biến của file giao diện xml
	@FXML
	private Label labelBan;
	@FXML
	private TextArea textAreaTrucTuyen;
	@FXML
	private TextArea textAreaNoiDung;
	@FXML
	private TextField textFieldSoanThao;
	@FXML
	private TextField textUserName;
	@FXML
	private PasswordField textPassW;
	@FXML
	private ComboBox<String> comboBoxChonNguoiNhan;
	@FXML
	private Label lbEmail;
	@FXML
	private Label lbFullName;
	@FXML
	private Label lbgender;

	
	//khai báo các biến cục bộ của lớp
	private String serverName = "localhost";
	private int port = 4000;
	
	private ThreadNhapXuat t;
	
	@Override
	public void initialize(URL url, ResourceBundle res) {
		ketNoiMayChu();
	}
	
	//phương thức kết nối với server
	public void ketNoiMayChu() {
		try {
			t = new ThreadNhapXuat(serverName, port, labelBan, textAreaTrucTuyen, 
					textAreaNoiDung, textFieldSoanThao, comboBoxChonNguoiNhan, textUserName, 
					textPassW, lbFullName,lbEmail, lbgender);
			t.start();
		} catch (Exception e) {
			System.out.println("Lỗi: " + e.getMessage());
			return;
		}
	}
	
	// phương thức hành động đăg nhâp khi hành động đăng nhập được thực hiện
	public void hanhDongDangNhap(ActionEvent event) {
		t.dangNhap();
	}
	//Phương thức hanhDongGui xử lý gửi thông điệp khi nút gửi được nhận
	public void hanhDongGui(ActionEvent event) {
		t.gui();
	}
}
