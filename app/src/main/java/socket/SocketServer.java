package socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
     private final static int BUFFER_SIZE = 1024;
    
    public static void main(String[] args){

        // 서버 인스턴스 생성(try-with : ServerSocket은 autocloseable을 상속 받음)
        try (ServerSocket server = new ServerSocket()){
            // 10001포트에 서버를 대기 시킴
            InetSocketAddress isa = new InetSocketAddress(10001);
            // 서버 인스턴스에 포트(10001)을 바인딩 함
            server.bind(isa);
            
            System.out.println("server initialize...........");

            // 클라이언트로부터 메시지를 대기하는 스레드 풀
            ExecutorService receiver = Executors.newCachedThreadPool(); 
            // 클라이언트 리스트
            List<Socket> clientList = new ArrayList<>();

            // 서버 대기
            while (true) {
                try {
                    // 클라이언트 접속
                    Socket client = server.accept();
                    // 클라이언트를 리스트에 추가한다.
                    clientList.add(client);
                    // 클라이언트 접속 정보 출력
                    System.out.println("Client connected IP address = " + client.getRemoteSocketAddress().toString());
                    // 클라리언트 스레트 풀 시작
                    receiver.execute(() -> {
                        // client가 종료되면, 소켓을  close한다.
                        // OutputStream과 InputStream을 받는다.
                        try (Socket thisClient = client;
                            OutputStream send = client.getOutputStream();
                            InputStream recv = client.getInputStream();) {
                            // 메시지 작성
                            String msg = "Hello server!\n";
                            // 메시지를 byte[]로 변환
                            byte[] msgBytes = msg.getBytes();
                            // client에 전송
                            send.write(msgBytes);
                            // 버퍼 객체 생성
                            StringBuffer sb = new StringBuffer();
                            // 메시지 대기 루프
                            while(true) {
                                // 버퍼 생성
                                msgBytes = new byte[BUFFER_SIZE];
                                // 메시지를 받는다.
                                recv.read(msgBytes, 0, msgBytes.length);
                                // byte --> String
                                msg = new String(msgBytes);
                                // 버퍼에 메시지 추가(\0 == null)
                                sb.append(msg.replace("\0", ""));
                                // 메시지가 개행일 경우
                                if (sb.length() > 2 
                                    && sb.charAt(sb.length() - 2) == '\r' 
                                    && sb.charAt(sb.length() - 1) == '\n'){
                                    // 메시지를 String으로 변환
                                    msg = sb.toString();
                                    // 버퍼 비우기
                                    sb.setLength(0);
                                    //메시지를 콘솔에 출력
                                    System.out.println(msg);
                                    //exit 메시지일 경우, 대기루프를 종료시킨다.
                                    if ("exit\r\n".equals(msg)) {
                                        break;
                                    }
                                    //echo 메시지 작성
                                    msg = "echo : " + msg + ">";
                                    //byte로 변환
                                    msgBytes = msg.getBytes();
                                    //클라이언트로 전송
                                    send.write(msgBytes);
                                }
                            }

                        } catch (Exception e2) {

                        } finally {
                            System.out.println("Client disconnected IP address = " + client.getRemoteSocketAddress().toString());
                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
