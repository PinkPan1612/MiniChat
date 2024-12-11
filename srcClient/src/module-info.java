module kiemThu {
	requires javafx.controls;
	requires javafx.fxml;
	
	opens chatAndLogin to javafx.graphics, javafx.fxml;
}
