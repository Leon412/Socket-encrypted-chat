import java.net.*; //Socket
import java.util.ArrayList;
import java.io.IOException;

/**
 * La classe {@code Server} serve ad intercettare le connessioni dei client 
 * ed inizializzare un ServerThread quando la connessione viene accettata.
 * <p>
 * Vengono memorizzati i thread avviati dal server e la tabella dei messaggi.
 * @author <a href="https://github.com/Leon412">Leonardo Panichi</a>
 */
public class Server {
    private static MessageBox mBox = new MessageBox(); //MessageBox, contenente mappe di messaggi e chiavi pubbliche per ogni User
    private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>(); //Arraylist di thread
    
    /**
     * Rimuove un istanza del {@link ServerThread} dall' {@link Server#threads Arraylist dei thread}.
     * @param s istanza di {@link ServerThread} da rimuovere dall'{@link Server#threads Arraylist dei thread}.
     */
    public static void close(ServerThread s) {
        threads.remove(s);
    }

    /**
     * Crea il socket del server e accetta le richieste di connessione
     * @param args Argomenti della linea di comando
     * @throws IOException Errori di connessione socket
     */
    public static void main(String[] args) throws IOException {
        int portNumber = 65535;
        
        while(true) {
            try (
                ServerSocket serverSocket = new ServerSocket(portNumber); //Crea il socket del server
            ) {
                Socket s = serverSocket.accept();
                threads.add(new ServerThread(s, mBox)); //Aggiunge il thread alla lista
                threads.get(threads.size()-1).start(); //Avvia l'ultimo thread
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}