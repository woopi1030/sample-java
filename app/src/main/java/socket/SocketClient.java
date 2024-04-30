package socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
    public static void main(String[] args) {
        System.out.println("hello");

        String ipAddress = "127.0.0.1";
        int port = 10001;

        try (Socket socket = new Socket(ipAddress, port)) {
            
            //서버에 write
            OutputStream output = socket.getOutputStream();
            byte[] data = "Hello Client".getBytes();
            output.write(data);

            //서버에서 read
            InputStream input = socket.getInputStream();
            input.read(data);
            System.out.println(new String(data));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
