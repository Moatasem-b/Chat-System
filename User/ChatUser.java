package User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import Chat.Chat;
import Message.Message;

public class ChatUser extends User {
    private ArrayList<ChatUser> friends = new ArrayList<>();
    private ArrayList<ChatUser> friendRequests = new ArrayList<>();
    private HashMap<String, Chat> chats = new HashMap<>();

    public ChatUser(String username, String hashedPassword) {
        super(username, hashedPassword);
    }

    public void addFriendRequest(ChatUser user) throws UserAlreadyExistsException {
        if (friends.contains(user)) {
            throw new UserAlreadyExistsException("You are already friends.");
        }
        if (user.friendRequests.contains(this)) {
            throw new UserAlreadyExistsException("The request has already been sent.");
        }
        if (friendRequests.contains(user)) {
            throw new UserAlreadyExistsException("This user has already sent you a friend request.");
        }
        if (user == this) {
            throw new UserAlreadyExistsException("can't send yourself a friend request.");
        }

        user.friendRequests.add(this);
    }

    public void cancelFriendRequest(ChatUser user) throws UserNotFoundException {
        if (!user.friendRequests.contains(this)) {
            throw new UserNotFoundException("The request not found.");
        }

        user.friendRequests.remove(this);
    }

    public void acceptFriendRequest(ChatUser user) throws UserNotFoundException {
        if (!friendRequests.contains(user)) {
            throw new UserNotFoundException("The request not found.");
        }

        friends.add(user);
        user.friends.add(this);
        friendRequests.remove(user);
    }

    public void denyFriendRequest(ChatUser user) throws UserNotFoundException {
        if (!friendRequests.contains(user)) {
            throw new UserNotFoundException("The request not found.");
        }


        friendRequests.remove(user);
    }

    public void removeFriend(ChatUser user) throws UserNotFoundException {
        if (!friends.contains(user)) {
            throw new UserNotFoundException("The friend not found.");
        }

        friends.remove(user);
        user.friends.remove(this);
    }

    public void sendMessage(ChatUser user, String message) {
        if (user == null || message == null) {
            return;
        }

        Chat senderChat = chats.get(user.username);
        Chat recipientChat = user.chats.get(this.username);

        if (senderChat == null || recipientChat == null) {
            Chat newChat = new Chat();
            addChat(user, newChat);
            user.addChat(this, newChat);
        }

        senderChat = chats.get(user.username);
        recipientChat = user.chats.get(this.username);

        senderChat.addMessage(this, user, message);
        if (!recipientChat.equals(senderChat)) {
            recipientChat.addMessage(this, user, message);
        }
    }

    public boolean addChat(ChatUser user, Chat chat) {
        if (user == null || chat == null || chats.containsKey(user.username)) {
            return false;
        }

        chats.put(user.username, chat);
        return true;
    }

    public void clearChatHistory(ChatUser user) throws ChatNotFoundException {
        if (!chats.containsKey(user.username)) {
            throw new ChatNotFoundException("The chat not found.");
        }

        chats.replace(user.username, new Chat());
    }

    public void removeChat(ChatUser user) throws ChatNotFoundException {
        if (!chats.containsKey(user.username)) {
            throw new ChatNotFoundException("The chat not found.");
        }

        chats.remove(user.username);
    }

    public void displayFriends() {
        for (ChatUser user : friends) {
            System.out.println(user.username);
        }

        if (friends.isEmpty()) {
            System.out.println("No friends yet.");
        }
    }

    public void displayFriendRequests() {
        for (ChatUser user : friendRequests) {
            System.out.println(user.username);
        }

        if (friendRequests.isEmpty()) {
            System.out.println("No pending requests.");
        }
    }

    public void displayChat(ChatUser user) {
        Chat chat = chats.get(user.username);

        if (chat == null) {
            System.out.println("No chat found.");
            return;
        }

        LocalDate previousMessageDate = null;
        
        if (!user.equals(this)) {
            System.out.printf("%-20s%10s%20s%n", user.username, "" ,"You");
        } else {
            System.out.printf("%50s%n", "You");
        }

        for (Message message : chat.getMessages()) {
            LocalDate messageDate = message.getTimestamp().toLocalDate();
            LocalTime messageTime = message.getTimestamp().toLocalTime();
            String hoursMinutes = messageTime.format(DateTimeFormatter.ofPattern("HH:mm"));

            if (!messageDate.equals(previousMessageDate)) {
                System.out.printf("%30s%n", messageDate.toString());
            }
            if (message.getSender().equals(this)) {
                System.out.printf("%44s%s%s%n", "[", hoursMinutes, "]");
                System.out.printf("%50s%n", message.getContent());
            } else {
                System.out.println("[" + hoursMinutes + "]\n" + message.getContent());
            }

            previousMessageDate = messageDate;
        }
    }
}