import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

// Διεπαφή για τον εξυπηρετητή(Server)
public interface ServerInterface extends Remote {
    // Δημιουργία νέου λογαριασμού
    int createAccount(String username) throws RemoteException;

    // Εμφάνιση λίστας με όλους τους λογαριασμούς
    List<String> showAccounts(int authToken) throws RemoteException;

    // Αποστολή μηνύματος σε έναν άλλο χρήστη
    String sendMessage(int authToken, String recipient, String messageBody) throws RemoteException;

    // Εμφάνιση όλων των μηνυμάτων (inbox) για έναν χρήστη
    List<String> showInbox(int authToken) throws RemoteException;

    // Ανάγνωση συγκεκριμένου μηνύματος
    String readMessage(int authToken, int messageId) throws RemoteException;

    // Διαγραφή συγκεκριμένου μηνύματος
    String deleteMessage(int authToken, int messageId) throws RemoteException;
}
