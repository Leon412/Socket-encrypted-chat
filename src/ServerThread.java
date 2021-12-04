import java.net.*;
import java.util.HashMap;
import java.io.*;

public class ServerThread extends Thread{
    private Socket s = null;
    private MessageBox mBox;
    private HashMap<String, String> commandList = new HashMap<String, String>();
    
    private String clientKey = null;
    private String userName = null;

    {
        commandList.put("list", "Visualizza la lista dei possibili riceventi\r\n\r\nLIST");
        commandList.put("send", "Invia un messaggio alla persona indicata\r\n\r\nSEND [destinatario] [messaggio]");
        commandList.put("receive", "Scrive i messaggi indirizzati a te\r\n\r\nRECEIVE");
        commandList.put("getkey", "Scrive la chiave dello user specificato\r\n\r\nGETKEY");
        commandList.put("quit", "Esce dal programma\r\n\r\nQUIT");
        commandList.put("help", "Fornisce la guida per i comandi\r\n\r\nHELP [comando]\r\n\r\n\tcomando - visualizza informazioni di guida per il comando.");
    }

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
                out.println("<Server> Scegli lo username (no spazi): ");
                out.println("INPUT");
                userName = in.readLine();
            }while(userName.contains(" ") || mBox.contains(userName));
            out.println("SENDKEY");
            clientKey = in.readLine();
            mBox.newUser(userName, clientKey);
            out.println("<Server> Benvenuto " + userName);
            //log in
            out.println("Digitare help per aiuto");
            out.println("INPUTC");
            while(!(line = in.readLine()).equals("QUIT")) {
                String lineArray[] = line.split(" ", 3);
                System.out.println(lineArray[0]);
                switch (lineArray[0].toLowerCase()) {
                case "list":
                    out.println(mBox.listUsers());
                    break;
                case "send":
                    if(lineArray.length < 3) {
                        out.println("<Server> sintassi errata");
                    }
                    else if(!mBox.send(lineArray[1], userName, lineArray[2])) {
                        out.println("<Server> si e' verificato un errore con l'invio");
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
                        out.println("<Server> nessun nuovo messaggio :(");
                    }
                    break;
                case "getkey":
                    if(lineArray.length < 2) {
                        out.println("<Server> sintassi errata");
                    }
                    else if(mBox.getKey(lineArray[1]) != null) {
                        out.println(mBox.getKey(lineArray[1]));
                    }
                    else {
                        out.println("<Server> sintassi errata");
                    }
                    break;
                case "help":
                    if(lineArray.length < 2) {
                        out.println("Per ulteriori informazioni su uno specifico comando, digitare HELP nome comando.\r\nLIST\tVisualizza la lista dei possibili riceventi\r\nSEND\tInvia una messaggio alla persona indicata\r\nRECEIVE\tScrive i messaggi indirizzati a te\r\nGETKEY\tScrive la chiave dello user specificato\r\nQUIT\tEsce dal programma\r\nHELP\tFornisce la guida per i comandi");
                    }
                    else {
                        if(commandList.get(lineArray[1].toLowerCase()) != null) {
                            out.println(commandList.get(lineArray[1].toLowerCase()));
                        }
                        else {
                            out.println("Comando non supportato dalla utilità di Guida");
                        }
                    }
                    break;
                case "quit":
                    out.println("sei sicuro? (s/n)");
                    out.println("INPUT");
                    if(in.readLine().charAt(0) == 's')
                        out.println("QUIT");
                    break;
                default:
                    out.println("<Server> comando non trovato");
                    break;
                }
                out.println("INPUTC");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        mBox.removeUser(userName);
        Server.close(this);
    }
}