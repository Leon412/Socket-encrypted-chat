import java.util.HashMap;                   //libreria per utilizzo metodi mappe
import java.util.LinkedList;                //libreria per utilizzo struttura dati LinkedList
import java.util.Queue;                     //libreria per uso di code
import java.time.LocalDateTime;             //libreria per utilizzo di ore e minuti

public class MessageBox {
    private HashMap<String, Queue<Message>> mb = new HashMap<String, Queue<Message>>();         //mappa che contiene i messaggi, con indice = ricevente
    private HashMap<String, String> pk = new HashMap<String, String>();                         //mappa delle chiavi pubbliche, con inice lo username

    //metodo che inserisce un nuovo user
    public synchronized void newUser(String userName, String publicKey) {
        mb.put(userName, new LinkedList<>());           //inizializza la coda dei messaggi ricevuti dello user
        pk.put(userName, publicKey);                    //inserisce nella mappa delle chiavi la chiave che viene inviata dal Client con il rispettivo user
    }

    public synchronized void removeUser(String userName) {
        if(mb.containsKey(userName)) {
            mb.remove(userName);
            pk.remove(userName);
        }
        else {
            System.out.println("problema grave");
        }
    }
    //funzione che controlla se uno user ha messaggi
    public boolean hasMessageFor(String userName) {
        if((!mb.containsKey(userName)) || (mb.get(userName).isEmpty()))     //se il destinataerio non è presente, o la sua lista di messaggi ricevuti è vuota
            return false;
        return true;
    }

    //funzione che legge ed in seguito elimina l'ultimo messaggio nella lista dei messaggi ricevuti di user
    public synchronized Message getLastMessageFor(String userName) {
        return mb.get(userName).poll();                                 //legge l'ultimo messaggio nella coda di username, poi lo elimina (.poll)
    }

    public String listUsers() {
        return mb.keySet().toString();          //restituisce la lista degli utenti e la trasforma in una stringa [nome, nome, ...]
    }

    //classe che aggiunge un nuovo messaggio alla lista del ricevitore
    public synchronized boolean send(String receiver, String sender, String msg) {
        if(!mb.containsKey(receiver)){          //se non è presente il ricevitore a cui si fa riferimento restituisce falso
            return false;
        }
        mb.get(receiver).add(new Message(sender, msg, LocalDateTime.now()));    //alla coda dei messaggi del ricevente aggiunge un nuovo messaggio
        return true;
    }

    public boolean contains(String userName) {
        if(mb.containsKey(userName))               //controlla se esiste un utente nella mappa con lo username identificato con 'userName'
            return true;
        return false;
    }

    public String getKey(String userName) {
        return pk.get(userName);                //restituisce la chiave pubblica di 'userName'
    }
}
