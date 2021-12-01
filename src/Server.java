import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
  public static void main(String[] args) {
    ServerSocket serversocket = null;
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintWriter out = null;
    final Scanner sc = new Scanner(System.in);

    try{
        serversocket = new ServerSocket(5000);
        clientSocket = serversocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream()); //sends data to client
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // receives data from client
    } catch (IOException e){
        e.printStackTrace();
    }
      PrintWriter finalOut = out;
      Thread sender = new Thread(new Runnable() { //thread to send messages
          String msg;
          @Override
          public void run() {
              while(true){
                  msg = sc.nextLine();
                  assert finalOut != null;
                  finalOut.println(msg);
                  finalOut.flush();
              }

          }
      });
    sender.start();

      PrintWriter finalOut1 = out;
      BufferedReader finalIn = in;
      Socket finalClientSocket = clientSocket;
      ServerSocket finalServerSocket = serversocket;
      Thread receive = new Thread(new Runnable() { // thread to receive messages
        String msg;
        @Override
        public void run() {
            try{
            msg = finalIn.readLine();
            while (msg != null){
                System.out.println("Client : " + msg);
                    msg = finalIn.readLine();
            }
            System.out.println("Client Disconnected");
            finalOut1.close();
            finalClientSocket.close();
            finalServerSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    });
    receive.start();
    }

}
