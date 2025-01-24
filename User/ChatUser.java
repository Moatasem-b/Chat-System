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

    public ChatUser(String username, String password) {
        super(username, password);
    }

    public boolean addFriendRequest(ChatUser user) throws UserAlreadyExistsException {
        if (user == null) {
            return false;
        }
        if (friends.contains(user)) {
            throw new UserAlreadyExistsException("You are already friends.");
        }
        if (user.friendRequests.contains(this)) {
            throw new UserAlreadyExistsException("The request has already been sent.");
        }
        if (friendRequests.contains(user)) {
            throw new UserAlreadyExistsException("This user has already sent you a friend request.");
        }

        user.friendRequests.add(this);
        return true;
    }

    public boolean unsendFriendRequest(ChatUser user) {
        if (user == null) {
            return false;
        }

        return user.friendRequests.remove(this);
    }

    public boolean cancelFriendRequest(ChatUser user) {
        if (user == null) {
            return false;
        }

        return friendRequests.remove(user);
    }

    public boolean acceptFriendRequest(ChatUser user) {
        if (user == null || !friendRequests.contains(user)) {
            return false;
        }

        friends.add(user);
        user.friends.add(this);
        cancelFriendRequest(user);

        return true;    // return cancelFriendRequest(user);

        // if (!cancelFriendRequest(user)) {
        //     return false;
        // }

        // friends.add(user);
        // user.friends.add(this);
        // return true;
    }

    public boolean removeFriend(ChatUser user) {
        if (user == null || !friends.contains(user)) {
            return false;
        }

        friends.remove(user);
        user.friends.remove(this);
        return true;
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

    // public Chat createNewChat(ChatUser user) {
    //     if (chats.containsKey(user.username)) {
    //         return null;
    //     }

    //     Chat newChat = new Chat();
    //     return newChat;
    // }

    public boolean addChat(ChatUser user, Chat chat) {
        if (user == null || chat == null || chats.containsKey(user.username)) {
            return false;
        }

        chats.put(user.username, chat);
        return true;
    }

    public boolean clearChatHistory(ChatUser user) {
        if (user == null || chats.replace(user.username, new Chat()) == null) {
            return false;
        }

        return true;
    }

    public boolean removeChat(ChatUser user) {
        if (user == null || chats.remove(user.username) == null) {
            return false;
        }

        return true;
    }

    public void displayFriends() {
        if (friends.isEmpty()) {
            System.out.println("No friends yet.");
            return;
        }
        
        for (ChatUser user : friends) {
            System.out.println(user.username);
        }
    }

    public void displayFriendRequests() {
        if (friendRequests.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (ChatUser user : friendRequests) {
            System.out.println(user.username);
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