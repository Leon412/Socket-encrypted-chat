import java.net.*;        //Socket
import java.util.HashMap; //Mappe
import java.util.ArrayList;
import java.io.IOException;

/**
 * La classe {@code Server} serve ad intercettare le connessioni dei client.
 * ed avviare un {@link ServerThread} quando la connessione viene accettata.
 * <p>
 * Vengono memorizzati i thread avviati dal server, la mappa dei messaggi e la mappa delle descrizioni dei comandi che possono essere eseguiti dal client.
 * @author <a href="https://github.com/Leon412">Leonardo Panichi</a>
 */
public class Server {
    private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();     //Arraylist di thread
    private static MessageBox mBox = new MessageBox();                                  //MessageBox contenente mappe di messaggi e chiavi pubbliche per ogni User
    private static HashMap<String, String> commandList = new HashMap<String, String>(); //Mappa delle descrizioni dei comandi che possono essere eseguiti dal client
                                                                                        //L'indice e' il nome del comando, l'argomento e' la descrizione del comando

    /**
     * Rimuove un istanza del {@link ServerThread} dall' {@link Server#threads Arraylist dei thread}.
     * @param s istanza di {@link ServerThread} da rimuovere dall'{@link Server#threads Arraylist dei thread}.
     */
    public static void close(ServerThread s) {
        threads.remove(s);
    }

    /**
     * Crea il socket del server e accetta le richieste di connessione.
     * @param args Argomenti della linea di comando.
     * @throws IOException Errori di connessione socket.
     */
    public static void main(String[] args) throws IOException {
        int portNumber = 65535;

        //Aggiunge le descrizioni dei comandi disponibili alla mappa dei comandi
        commandList.put("list", "Visualizza la lista dei possibili riceventi\r\n\r\nLIST");
        commandList.put("send", "Invia un messaggio criptato alla persona indicata\r\n\r\nSEND [destinatario] [messaggio]\r\n\r\n\tdestinatario - username di un utente online\r\n\tmessaggio - messaggio da inviare");
        commandList.put("receive", "Scrive i messaggi indirizzati a te\r\n\r\nRECEIVE");
        commandList.put("getkey", "Scrive la chiave dello user specificato\r\n\r\nGETKEY");
        commandList.put("quit", "Esce dal programma\r\n\r\nQUIT");
        commandList.put("help", "Fornisce la guida per i comandi\r\n\r\nHELP [comando]\r\n\r\n\tcomando - visualizza informazioni di guida per il comando.");
        
        while(true) {
            try (
                ServerSocket serverSocket = new ServerSocket(portNumber); //Crea il socket del server
            ) {
                Socket s = serverSocket.accept();
                threads.add(new ServerThread(s, mBox, commandList)); //Aggiunge il thread alla lista
                threads.get(threads.size()-1).start(); //Avvia l'ultimo thread
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}