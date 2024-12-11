module ZaloChat {
	requires javafx.controls;
	requires java.sql;
	exports server to javafx.graphics, javafx.fxml;
	exports data to javafx.graphics, javafx.fxml;
}
