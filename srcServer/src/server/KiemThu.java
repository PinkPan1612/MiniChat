package server;

import java.io.IOException;

public class KiemThu {
    public static void main(String[] args) {
        int port = 4000;
       
        try {
            Server server = new Server(port);
            server.chay();
        } catch (IOException e) {
            System.out.println("Lỗi rồi nè: " + e.getMessage());
        }
    }
}