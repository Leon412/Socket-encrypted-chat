import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm");

    private String encryptedMsg;
    private String sender;
    private LocalDateTime sendingDate;

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

    public String formattedMessage(){
        return "[" + dtf.format(sendingDate) + "]" + "<" + sender + "> " + encryptedMsg;
    }
}
