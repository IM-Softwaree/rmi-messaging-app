import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// Υλοποίηση του εξυπηρετητή(Server) για την ανταλλαγή μηνυμάτων
public class Server extends UnicastRemoteObject implements ServerInterface {
    private Map<String, Account> accounts; // Δομή Map για την αποθήκευση ονομάτων χρηστών και των λογαριασμών
    private int authTokenCounter = 1000; // Μετρητής για τη δημιουργία μοναδικών authTokens

    public Server() throws RemoteException {
        super();
        accounts = new HashMap<>(); // Αρχικοποίηση του HashMap
    }

    // Δημιουργία νέου λογαριασμού
    @Override
    public synchronized int createAccount(String username) throws RemoteException {
        // Έλεγχος αν το όνομα χρήστη είναι έγκυρο
        if (!username.matches("[A-Za-z0-9_]+")) {
            return -1; // Invalid username
        }
        // Έλεγχος αν ο λογαριασμός υπάρχει ήδη
        if (accounts.containsKey(username)) {
            return -2; // User already exists
        }
        // Δημιουργία νέου λογαριασμού
        int authToken = authTokenCounter++;
        accounts.put(username, new Account(username, authToken));
        return authToken; // Επιστροφή του κωδικού authToken
    }

    // Επιστροφή της λίστας με όλους τους λογαριασμούς
    @Override
    public synchronized List<String> showAccounts(int authToken) throws RemoteException {
        if (!isValidAuthToken(authToken)) {
            throw new RemoteException("Invalid Auth Token"); // Έλεγχος για έγκυρο authToken
        }
        List<String> accountList = new ArrayList<>();
        for (String user : accounts.keySet()) {
            accountList.add(user);
        }
        return accountList; // Επιστροφή της λίστας με τα ονόματα χρηστών
    }

    // Αποστολή μηνύματος σε άλλον χρήστη
    @Override
    public synchronized String sendMessage(int authToken, String recipient, String messageBody) throws RemoteException {
        Account sender = getAccountByToken(authToken); // Έλεγχος για έγκυρο authToken
        if (sender == null) return "Invalid Auth Token";

        Account receiver = accounts.get(recipient); // Έλεγχος αν υπάρχει ο παραλήπτης
        if (receiver == null) return "User does not exist";

        // Δημιουργία και προσθήκη μηνύματος στη λίστα με τα μηνύματα του παραλήπτη
        Message message = new Message(sender.getUsername(), recipient, messageBody);
        receiver.addMessage(message);
        return "OK";
    }

    // Επιστροφή όλων των μηνυμάτων για έναν συγκεκριμένο χρήστη
    @Override
    public synchronized List<String> showInbox(int authToken) throws RemoteException {
        Account account = getAccountByToken(authToken); // Έλεγχος για έγκυρο authToken
        if (account == null) return null;

        // Δημιουργία της λίστας των μηνυμάτων (inbox)
        List<String> inbox = new ArrayList<>();
        for (Message msg : account.getMessageBox()) {
            inbox.add((msg.isRead() ? msg.getSender() + "" : msg.getSender() + "*"));
        }
        return inbox;
    }

    // Ανάγνωση συγκεκριμένου μηνύματος από το inbox
    @Override
    public synchronized String readMessage(int authToken, int messageId) throws RemoteException {
        Account account = getAccountByToken(authToken);
        if (account == null) return "Invalid Auth Token";

        // Έλεγχος αν υπάρχει το μήνυμα
        if (messageId < 0 || messageId >= account.getMessageBox().size()) {
            return "Message ID does not exist";
        }

        // Κάνω του μήνυμα ως διαβασμένο και επιστρέφω το περιεχομένου του
        Message msg = account.getMessageBox().get(messageId);
        msg.markAsRead();
        return "(" + msg.getSender() + ")" + msg.getBody();
    }

    // Διαγραφή μηνύματος από το inbox
    @Override
    public synchronized String deleteMessage(int authToken, int messageId) throws RemoteException {
        Account account = getAccountByToken(authToken); // Έλεγχος για έγκυρο authToken
        if (account == null) return "Invalid Auth Token";

        // Έλεγχος αν υπάρχει το μήνυμα
        if (messageId < 0 || messageId >= account.getMessageBox().size()) {
            return "Message does not exist";
        }

        // Διαγραφή του μηνύματος από τη λίστα των μηνυμάτων του χρήστη
        account.getMessageBox().remove(messageId);
        return "OK";
    }

    // Έλεγχος αν ο κωδικός AuthToken είναι έγκυρος
    private boolean isValidAuthToken(int authToken) {
        return accounts.values().stream().anyMatch(account -> account.getAuthToken() == authToken);
    }

    // Εύρεση λογαριασμού βάσει κωδικού AuthToken
    private Account getAccountByToken(int authToken) {
        return accounts.values().stream().filter(account -> account.getAuthToken() == authToken).findFirst().orElse(null);
    }

    public static void main(String[] args) {
        try {
            // Ελέγχουμε αν έχει δοθεί αριθμός θύρας από τη γραμμή εντολών
            if (args.length < 1) {
                System.out.println("Usage: java Server <port>");
                return;
            }

            int port = Integer.parseInt(args[0]);

            Server server = new Server();

            // Δημιουργούμε το RMI Registry στη δοσμένη θύρα
            Registry registry = LocateRegistry.createRegistry(port);

            // Εδώ γίνεται η δέσμευση του αντικειμένου στο RMI registry με το όνομα "Server"
            registry.rebind("Server", server);

            System.out.println("Server is ready on port " + port);
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
