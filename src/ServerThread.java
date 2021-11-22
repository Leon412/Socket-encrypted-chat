import java.net.*;
import java.io.*;

public class ServerThread extends Thread{
    private Socket s = null;
    private MessageBox mBox;
    
    private String clientKey = null;
    private String userName = null;

    public ServerThread(Socket s, MessageBox mBox) {
        this.s=s;
        this.mBox = mBox;
    }

    public void run() {
        String line = null;
        try(
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        ) {
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
                    if(lineArray.length < 3) {
                        out.println("<Server> wrong syntax");
                    }
                    else if(mBox.contains(lineArray[1])) {
                        mBox.send(lineArray[1], userName, lineArray[2]);
                        out.println("DECRYPT");
                        out.println(mBox.getLastMessageFor(userName).formattedMessage());
                    }
                    else {
                        out.println("<Server> Username not found");
                    }
                    break;
                case "/r":
                    
                    break;
                case "/help":
                    out.println("");
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