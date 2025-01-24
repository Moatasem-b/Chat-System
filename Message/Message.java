package Message;

import java.time.LocalDateTime;

import User.ChatUser;

public class Message {
    private ChatUser sender;
    private ChatUser recipient;
    private String content;
    private LocalDateTime timestamp;

    public Message(ChatUser sender, ChatUser recipient, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatUser getSender() {
        return sender;
    }

    public void setSender(ChatUser sender) {
        this.sender = sender;
    }

    public ChatUser getRecipient() {
        return recipient;
    }

    public void setRecipient(ChatUser recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public void display() {
        System.out.println("Sender: " + sender);
        System.out.println("Recipient: " + recipient);
        System.out.println("Timestamp: " + timestamp);
        System.out.println("Content: " + content);
    }  
}
