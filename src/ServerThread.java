import java.net.*;       //libreria per socket
import java.util.HashMap;       //libreria per utilizzo metodi mappe
import java.io.*;            //libreria per gestire eccezzioni

public class ServerThread extends Thread{
    private Socket s = null;
    private MessageBox mBox;
    private HashMap<String, String> commandList = new HashMap<String, String>();        //istanzio la mappa per poter istanziare la descrizione dei comandi
                                                                                        //indice è il nome del comando, contenuto è la descrizione del comando
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
            do {                                                                        //ciclo do while che aspetta che l'utente inserisca lo username senza spazi
                                                                                        // e che non sia già presente nella lista
                out.println("<Server> Scegli lo username (no spazi): ");
                out.println("INPUT");       //invia al client il comando "INPUT"
                userName = in.readLine();
            }while(userName.contains(" ") || mBox.contains(userName));
            out.println("SENDKEY");     //invia al client il comando "SENDKEY"
            clientKey = in.readLine();
            mBox.newUser(userName, clientKey);      //inserisce un nuovo utente
            out.println("<Server> Benvenuto " + userName);
            //log in
            out.println("Digitare help per aiuto");
            out.println("INPUTC");
            while(!(line = in.readLine()).equals("QUIT")) {     //ciclo do while che termina quando l'utente inserisce il comando "QUIT"
                String lineArray[] = line.split(" ", 3);        //inserimento in un array della stringa inviata dal client divisa ad ogni spazio per massimo 3 volte
                System.out.println(lineArray[0]);               //stampa comando sul server
                switch (lineArray[0].toLowerCase()) {       //switch comando
                    case "list":                                //caso: il comando è list
                    out.println(mBox.listUsers());           //invia la lista degli utenti disponibili
                    break;
                    case "send":            //caso: il comando è send
                    if(lineArray.length < 3) {            //se l'utente non ha scritto nel formato [comando destinatario messaggio]  
                                                            
                        out.println("<Server> sintassi errata");
                    }
                    else if(!mBox.send(lineArray[1], userName, lineArray[2])) {             //se non invia il messaggio
                        out.println("<Server> si e' verificato un errore con l'invio");
                    }
                    break;
                case "receive":                                //caso: il comando è receive
                    if(mBox.hasMessageFor(userName)) {         //se ci sono messaggi per quello user
                        do {            
                            out.println("DECRYPT");             //invia il comando DECRYPT
                            out.println(mBox.getLastMessageFor(userName).formattedMessage()); //invia al client l'ultimo messaggio ricevuto dal corrispetivo utente formattato correttamente
                        }while(mBox.hasMessageFor(userName));       //finchè ci sono messaggi per quell'utente
                    }
                    else {
                        out.println("<Server> nessun nuovo messaggio :(");
                    }
                    break;
                    case "getkey":          //caso: getkey
                    if(lineArray.length < 2) {      //se l'utente non ha scritto nel formato [comando username]  
                        out.println("<Server> sintassi errata");
                    }
                    else if(mBox.getKey(lineArray[1]) != null) {   //quando esiste lo username del quale si richiede la chiave     
                        out.println(mBox.getKey(lineArray[1]));     //invia la chiave dello username al client
                    }
                    else {
                        out.println("<Server> username non trovato");
                    }
                    break;
                    case "help":             //caso: help
                    if(lineArray.length < 2) {  //se l'utente ha inserito solo help 
                        out.println("Per ulteriori informazioni su uno specifico comando, digitare HELP nome comando.\r\nLIST\tVisualizza la lista dei possibili riceventi\r\nSEND\tInvia una messaggio alla persona indicata\r\nRECEIVE\tScrive i messaggi indirizzati a te\r\nGETKEY\tScrive la chiave dello user specificato\r\nQUIT\tEsce dal programma\r\nHELP\tFornisce la guida per i comandi");
                    }
                    else {
                        if(commandList.get(lineArray[1]) != null) {   //se esiste il comando 
                            out.println(commandList.get(lineArray[1].toLowerCase()));   //invia la descrizione del comando al client
                        }
                        else {
                            out.println("Comando non supportato dalla utilità di Guida");
                        }
                    }
                    break;
                    case "quit":        //caso: quit
                    out.println("sei sicuro? (s/n)");
                    out.println("INPUT");
                    if(in.readLine().charAt(0) == 's')  //se la risposta inizia con s allora viene eseguito il quit
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
        mBox.removeUser(userName);      //quando il client si disconnette rimuove le informazioni dell'utente e termina il thread
        Server.close(this);
    }
}
