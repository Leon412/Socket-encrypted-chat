import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDateTime;

public class MessageBox {
    private HashMap<String, Queue<Message>> mb = new HashMap<String, Queue<Message>>();
    private HashMap<String, String> pk = new HashMap<String, String>();

    public void newUser(String userName, String publicKey) {
        mb.put(userName, new LinkedList<>());
        pk.put(userName, publicKey);
    }

    public boolean hasMessageFor(String userName) {
        if((!mb.containsKey(userName)) || (mb.get(userName).size() == 0)) //effettivamente bo
            return false;
        return true;
    }

    public Message getLastMessageFor(String userName) {
        return mb.get(userName).poll();
    }

    public String listUsers() {
        return mb.keySet().toString();
    }

    public void send(String receiver, String sender, String msg) {
        if(!mb.containsKey(receiver)){
            System.out.println("username not found");
            return;
        }
        mb.get(receiver).add(new Message(sender, msg, LocalDateTime.now()));
    }

    public boolean contains(String line) {
        if(mb.containsKey(line))
            return true;
        return false;
    }
}
