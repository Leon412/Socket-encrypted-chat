import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ServerThread extends Thread{
    private Socket s = null;

    private MessageBox mBox;
    private ArrayList<ServerThread> threads = new ArrayList<ServerThread>();

    private PrintWriter out;
    private BufferedReader in;

    private String line = null;

    public ServerThread(Socket s, int c, ArrayList<ServerThread> threads, MessageBox mBox) {
        this.s=s;
        this.threads = threads;
        this.mBox = mBox;
    }

    public void run() {
        String clientKey = null;
        String userName = null;
        try {
            out = new PrintWriter(s.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            do {
                out.println("<Server> Choose your username (no spaces): ");
                out.println("INPUT");
                userName = in.readLine();
            }while(userName.contains(" ") || mBox.contains(userName));
            out.println("SENDKEY");
            clientKey = in.readLine();
            mBox.newUser(userName, clientKey);
            out.println("<Server> Benvenuto " + userName);
            //log in
            
            out.println("INPUT");
            while((line = in.readLine()) != null) {
                String lineArray[] = line.split(" ", 3);
                System.out.println(lineArray[0]);
                switch (lineArray[0]) {
                case "/list":
                    out.println(mBox.listUsers());
                    break;
                case "/send":
                    if(lineArray.length < 3){
                        out.println("<Server> wrong syntax");
                        break;
                    }
                    if(mBox.contains(lineArray[1])){
                        mBox.send(lineArray[1], userName, lineArray[2]);
                        out.println("DECRYPT");
                        out.println(mBox.getLastMessageFor(userName).formattedMessage());
                    }
                    else
                        out.println("<Server> Username not found");
                    break;
                case "/r":
                    
                    break;
                case "/help":

                    break;
                default:
                    out.println("<Server> Command not found");
                    break;
                }
                out.println("INPUT");
            }
            out.println("QUIT");
        } catch(IOException e) {
            e.printStackTrace();
        }
        
    }
}