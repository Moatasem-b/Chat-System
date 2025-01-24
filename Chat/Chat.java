package Chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import Message.Message;
import User.ChatUser;

public class Chat {
    ArrayList<Message> messages = new ArrayList<>();

    public void addMessage(ChatUser sender, ChatUser recipient, String content) {
        Message newMessage = new Message(sender, recipient, content, LocalDateTime.now());
        messages.add(newMessage);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void display() {
        for (Message message : messages) {
            message.display();
        }
    }
}
