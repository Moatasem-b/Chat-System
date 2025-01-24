package ChatSystem;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import User.*;

public class ChatSystem {
    private Admin admin = new Admin("admin", "admin");
    private HashMap<String, ChatUser> users = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public void register(String username, String password) throws UserAlreadyExistsException {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException("This username is already registered.");
        }

        ChatUser newUser = new ChatUser(username, password);
        users.put(username, newUser);
    }

    public void registerHandler() throws UserAlreadyExistsException {
        String username = promptUsername();
        String password = promptPassword();

        register(username, password);
    }

    public void registerPageHandler() {
        try {
            registerHandler();
            System.out.println("The account has been registered successfully.");
        } catch (UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    public User login(String username, String password) throws UserNotFoundException {
        if (admin.checkUsername(username) && admin.checkPassword(password)) {
            return admin;
        }

        User user = users.get(username);

        if (user == null || !user.checkPassword(password)) {
            throw new UserNotFoundException("Incorrect Username or Password.");
        }

        return user;
    }

    public User loginHandler() throws UserNotFoundException {
        String username = promptUsername();
        String password = promptPassword();

        return login(username, password);
    }

    public void loginPageHandler() {
        try {
            User user = loginHandler();

            if (user instanceof Admin) {
                System.out.println("Admin.");
            }
            else if (user instanceof ChatUser chatUser) {
                chatUserPageHandler(chatUser);
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayHomePage() {
        System.out.println("\n-----------------");
        System.out.println("1. Register.");
        System.out.println("2. Login.");
        System.out.println("3. Quit.");
        System.out.println("-----------------");
    }

    public void HomePageHandler() {
        System.out.print("\nWelcome to Chatting System");
        int choice;
        do {
            displayHomePage();
            choice = promptChoice(1, 3);
            System.out.println();
            userChoiceHandler(choice);
        } while (choice != 3);
    }

    public void userChoiceHandler(int choice) {
        switch (choice) {
            case 1:
                registerPageHandler();
            break;
            case 2:
                loginPageHandler();
            break;
            case 3:
                System.out.println("\nThanks for using our app.");
            break;
        }
    }

    public void displayChatUserMenu() {
        System.out.println("\n-----------------------------");
        System.out.println("1. Add a new friend.");
        System.out.println("2. Cancel friend request.");
        System.out.println("3. Accept friend request.");
        System.out.println("4. Remove friend.");
        System.out.println("5. Start chat.");
        System.out.println("6. Display Chat.");
        System.out.println("7. Clear chat history.");
        System.out.println("8. Delete chat.");
        System.out.println("9. Display friend requests.");
        System.out.println("10. Display friends.");
        System.out.println("11. Log out.");
        System.out.println("-----------------------------");
    }

    public void chatUserPageHandler(ChatUser user) {
        System.out.print("\nWelcome back, " + user.getUsername() + "!");
        int choice;
        do {
            displayChatUserMenu();
            choice = promptChoice(1, 11);
            System.out.println();
            chatUserChoiceHandler(user, choice);
        } while (choice != 11);
    }

    public void chatUserChoiceHandler(ChatUser user, int choice) {
        switch (choice) {
            case 1:
                actioinHandler(user, "AddFriendRequest");
            break;
            case 2:
                actioinHandler(user, "CancelFriendRequest");
            break;
            case 3:
                actioinHandler(user, "AcceptFriendRequest");
            break;
            case 4:
                actioinHandler(user, "RemoveFriend");
            break;
            case 5:
                actioinHandler(user, "StartChat");
            break;
            case 6:
                actioinHandler(user, "DisplayChat");;
            break;
            case 7:
                actioinHandler(user, "ClearChatHistory");
            break;
            case 8:
                actioinHandler(user, "RemoveChat");
            break;
            case 9:
                user.displayFriendRequests();
            break;
            case 10:
                user.displayFriends();
            break;
            case 11:
                System.out.println("\nLogged out.");
            break;
        }
    }

    public void actioinHandler(ChatUser user, String action) {
        ChatUser recipient = getChatUser();

        try {
            validateUser(recipient);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        switch (action) {
            case "AddFriendRequest":
                addFriendRequest(user, recipient);
            break;
            case "CancelFriendRequest":
                cancelFriendRequest(user, recipient);
            break;
            case "AcceptFriendRequest":
                acceptFriendRequest(user, recipient);
            break;
            case "RemoveFriend":
                removeFriend(user, recipient);
            break;
            case "StartChat":
                startChat(user, recipient);
            break;
            case "DisplayChat":
                user.displayChat(recipient);
            break;
            case "ClearChatHistory":
                clearChatHistory(user, recipient);
            break;
            case "RemoveChat":
                removeChat(user, recipient);
            break;
        } 
    }

    public void addFriendRequest(ChatUser sender, ChatUser recipient) {
        try {
            sender.addFriendRequest(recipient);
            System.out.println("The request has been sent successfully.");
        } catch (UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cancelFriendRequest(ChatUser sender, ChatUser recipient) {
        if (sender.cancelFriendRequest(recipient)) {
            System.out.println("The request has been cancelled successfully.");
        } else {
            System.out.println("The request not found.");
        }
    }

    public void acceptFriendRequest(ChatUser sender, ChatUser recipient) {
        if (sender.acceptFriendRequest(recipient)) {
            System.out.println("The request has been accepted successfully.");
        } else {
            System.out.println("The request not found.");
        }
    }

    public void removeFriend(ChatUser sender, ChatUser recipient) {
        if (sender.removeFriend(recipient)) {
            System.out.println("The friend has been removed successfully.");
        } else {
            System.out.println("The friend not found.");
        }
    }

    public void startChat(ChatUser sender, ChatUser recipient) {
        String message;
        scanner.nextLine();
        do {
            message = promptMessage();
            if (!message.isEmpty()) {
                sender.sendMessage(recipient, message);
            }
        } while (!message.isEmpty());
    }

    public void clearChatHistory(ChatUser sender, ChatUser recipient) {
        if (sender.clearChatHistory(recipient)) {
            System.out.println("The chat has been cleared successfully.");
        } else {
            System.out.println("The chat not found.");
        }
    }

    public void removeChat(ChatUser sender, ChatUser recipient) {
        if (sender.removeChat(recipient)) {
            System.out.println("The chat has been deleted successfully.");
        } else {
            System.out.println("The chat not found.");
        } 
    }

    private String promptUsername() {
        System.out.print("Enter username: ");
        String username = scanner.next();

        return username;
    }

    private String promptPassword() {
        System.out.print("Enter password: ");
        String password = scanner.next();

        return password;
    }

    private String promptMessage() {
        System.out.print("Enter message (Enter to quit): ");
        String message = scanner.nextLine();

        return message;
    }

    private ChatUser getChatUser() {
        String username = promptUsername();
        ChatUser user = users.get(username);

        return user;
    }

    private void validateUser(User user) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("User not found.");
        }
    }

    private int promptChoice(int start, int end) {
        int choice = -1;
        boolean choiceValidation = false;

        do {
            System.out.print("Enter choice between " + start + " and " + end + ": ");

            try {
                choice = scanner.nextInt();
                choiceValidation = validateChoice(choice, start, end);
  
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Pleasee, Try again.\n");
                scanner.nextLine();
            } catch (InvalidChoiceException e) {
                System.out.println(e.getMessage());
            }

        } while (!choiceValidation);

        return choice;
    }

    private boolean validateChoice(int choice, int start, int end) throws InvalidChoiceException  {
        if (choice < start || choice > end) {
            throw new  InvalidChoiceException("Invalid choice. Please, Try again.\n");
        }

        return true;
    }

    public static void main(String[] args) throws Exception{
        ChatSystem chatSystem = new ChatSystem();
        chatSystem.register("Ahmed", "1");
        chatSystem.register("Mona", "2");
        chatSystem.addFriendRequest(chatSystem.users.get("Ahmed"), chatSystem.users.get("Mona"));
        chatSystem.acceptFriendRequest(chatSystem.users.get("Mona"), chatSystem.users.get("Ahmed"));
        chatSystem.users.get("Ahmed").sendMessage(chatSystem.users.get("Mona"), "Hello, Mona!");
        chatSystem.users.get("Ahmed").sendMessage(chatSystem.users.get("Mona"), "How are you doing?");
        chatSystem.users.get("Mona").sendMessage(chatSystem.users.get("Ahmed"), "Hi! I'm fine. what about you?");
        chatSystem.HomePageHandler();
    }
}
