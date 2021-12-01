import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket clientSocket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        final Scanner sc = new Scanner(System.in);

        try{
            clientSocket = new Socket("127.0.0.1", 5000);
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
        Thread receive = new Thread(new Runnable() { // thread to receive messages
            String msg;
            @Override
            public void run() {
                try{
                    msg = finalIn.readLine();
                    while (msg != null){
                        System.out.println("Server : " + msg);
                        msg = finalIn.readLine();
                    }
                    System.out.println("Server Not Available");
                    finalOut1.close();
                    finalClientSocket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        receive.start();
    }

}
