import java.time.LocalDateTime;                     //libreria per utilizzo della data
import java.time.format.DateTimeFormatter;           //libreria per permettere la formattazione della data

public class Message {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm"); //formato yy/MM/dd (opzionale)  ofpattern (HH:mm) = formatta in HH:mm

    private String encryptedMsg;                     //messaggio criptato
    private String sender;                           //mittente
    private LocalDateTime sendingDate;               //data del momento di invio
    
    //costruttore
    public Message(String sender, String msg, LocalDateTime sendingDate) {
        this.encryptedMsg = msg;
        this.sender = sender;
        this.sendingDate = sendingDate;
    }
    //metodi set e get
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
    
    //metodo per formattare la data del messaggio
    public String formattedMessage(){
        return "[" + dtf.format(sendingDate) + "]" + "<" + sender + "> " + encryptedMsg;
    }
}
