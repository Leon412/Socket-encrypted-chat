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
            out.println("Digitare help per aiuto");
            out.println("INPUT");
            while((line = in.readLine()) != null) {
                String lineArray[] = line.split(" ", 3);
                System.out.println(lineArray[0]);
                switch (lineArray[0].toLowerCase()) {
                case "list":
                    out.println(mBox.listUsers());
                    break;
                case "send":
                    if(lineArray.length < 3) {
                        out.println("<Server> wrong syntax");
                    }
                    else if(mBox.contains(lineArray[1])) {
                        mBox.send(lineArray[1], userName, lineArray[2]);
                    }
                    break;
                case "receive":
                    if(mBox.hasMessageFor(userName)) {
                        do {
                            out.println("DECRYPT");
                            out.println(mBox.getLastMessageFor(userName).formattedMessage());
                        }while(mBox.hasMessageFor(userName));
                    }
                    else {
                        out.println("<Server> no new messages for you :(");
                    }
                    break;
                case "getkey":
                    if(lineArray.length < 2) {
                        out.println("<Server> wrong syntax");
                    }
                    else if(mBox.getKey(lineArray[1]) != null) {
                        out.println(mBox.getKey(lineArray[1]));
                    }
                    else {
                        out.println("<Server> wrong syntax");
                    }
                    break;
                case "help":
                    if(lineArray.length < 2) {
                        out.println("Per ulteriori informazioni su uno specifico comando, digitare HELP nome comando.\r\nLIST\tVisualizza la lista dei possibili riceventi\r\nSEND\tInvia una messaggio alla persona indicata\r\nRECEIVE\tScrive i messaggi indirizzati a te\r\nGETKEY\tRitorna la chiave dello user specificato\r\nHELP\tFornisce la guida per i comandi");
                    }
                    else {
                        //TODO fai l'help
                    }
                        
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