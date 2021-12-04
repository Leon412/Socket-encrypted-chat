import java.net.*;                       //libreria per socket
import java.util.ArrayList;              //libreria per arraylist
import java.io.*;                        //libreria per le eccezioni           
 
public class Server {
    private static MessageBox mBox = new MessageBox();                                              //tabella di messaggi
    private static ArrayList<ServerThread> threads = new ArrayList<ServerThread>();                 //arraylist di thread
    
    //chiusura del thread e rimuove la sua istanza nell'array
    public static void close(ServerThread s) {
        threads.remove(s);
    }

    public static void main(String[] args) throws IOException {
        int portNumber = 65535;
        
        while(true) {
            try (
                ServerSocket serverSocket = new ServerSocket(portNumber);                           //creazione socket
            ) {
                Socket s = serverSocket.accept();                  
                threads.add(new ServerThread(s, mBox));                                            //aggiunge alla lista il thread
                threads.get(threads.size()-1).start();                                             //fa partire l'ultimo thread                             
                
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
                break;
             
            }
        }
    }
}
