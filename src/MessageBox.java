import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDateTime;

public class MessageBox {
    private HashMap<String, Queue<Message>> mb = new HashMap<String, Queue<Message>>();
    private HashMap<String, String> pk = new HashMap<String, String>();

    public synchronized void newUser(String userName, String publicKey) {
        mb.put(userName, new LinkedList<>());
        pk.put(userName, publicKey);
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

    public boolean hasMessageFor(String userName) {
        if((!mb.containsKey(userName)) || (mb.get(userName).isEmpty()))
            return false;
        return true;
    }

    public synchronized Message getLastMessageFor(String userName) {
        return mb.get(userName).poll();
    }

    public String listUsers() {
        return mb.keySet().toString();
    }

    public synchronized boolean send(String receiver, String sender, String msg) {
        if(!mb.containsKey(receiver)){
            return false;
        }
        mb.get(receiver).add(new Message(sender, msg, LocalDateTime.now()));
        return true;
    }

    public boolean contains(String line) {
        if(mb.containsKey(line))
            return true;
        return false;
    }

    public String getKey(String userName) {
        return pk.get(userName);
    }
}