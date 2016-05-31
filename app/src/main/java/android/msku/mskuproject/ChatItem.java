package android.msku.mskuproject;

/*
* This class is used for storing messages received and sent via XMPP
* */
public class ChatItem {

    private String sender;
    private String message;

    public ChatItem(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
