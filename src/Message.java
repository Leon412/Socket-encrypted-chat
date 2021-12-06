import java.time.LocalDateTime; //Data e tempo
import java.time.format.DateTimeFormatter; //Formattazione di data e tempo

/**
 * La classe {@code Message} rappresenta un messaggio, memorizzandone il contenuto criptato, 
 * lo userName del mandante e la data e ora del momento di invio.
 * @author <a href="https://github.com/Leon412">Leonardo Panichi</a>
 */
public class Message {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm"); //Formattatore di data e ora
                                                                          //ofpattern (HH:mm) = formatta in HH:mm

    private String encryptedMsg;       //Messaggio criptato
    private String sender;             //Mandante
    private LocalDateTime sendingDate; //Data e tempo del momento di invio
    
    /**
     * Costruttore di {@code Message}.
     * @param sender Mandante del messaggio.
     * @param msg Messaggio criptato.
     * @param sendingDate Data e tempo del momento di invio.
     */
    public Message(String sender, String msg, LocalDateTime sendingDate) {
        this.encryptedMsg = msg;
        this.sender = sender;
        this.sendingDate = sendingDate;
    }

    public String getMsg() {
        return encryptedMsg;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public void setMsg(String msg) {
        this.encryptedMsg = msg;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSendingDate(LocalDateTime sendingDate) {
        this.sendingDate = sendingDate;
    }
    
    /**
     * Formatta il messaggio in:
     * <blockquote>
     *    [HH:mm]&#60;mandante.> Messaggio criptato
     * </blockquote>
     * @return Il messaggio formattato.
     */
    public String formattedMessage(){
        return "[" + dtf.format(sendingDate) + "]" + "<" + sender + "> " + encryptedMsg;
    }
}