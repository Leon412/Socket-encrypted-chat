import java.net.*;
import java.util.ArrayList;
import java.io.*;
 
public class Server {
    private static MessageBox mBox = new MessageBox();
    private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();
    
    public static void close(ServerThread s) {
        threads.remove(s);
    }

    public static void main(String[] args) throws IOException {
        int portNumber = 65535;
        
        while(true) {
            try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
            ) {
                Socket s = serverSocket.accept();
                threads.add(new ServerThread(s, mBox));
                threads.get(threads.size()-1).start();
                
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
                break;
             
            }
        }
    }
}
