import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Κλάση που αναπαριστά έναν λογαριασμό χρήστη
public class Account implements Serializable {
    private String username;   // Όνομα χρήστη
    private int authToken;     // Κωδικός αυθεντικοποίησης
    private List<Message> messageBox; // Λίστα με τα μηνύματα του χρήστη

    // Κατασκευαστής της κλάσης Account
    public Account(String username, int authToken) {
        this.username = username;
        this.authToken = authToken;
        this.messageBox = new ArrayList<>();  // Αρχικοποίηση του γραμματοκιβωτίου
    }

    // Επιστρέφει το όνομα χρήστη
    public String getUsername() {
        return username;
    }

    // Επιστρέφει το μοναδικό κωδικό για κάθε χρήστη(Auth Token)
    public int getAuthToken() {
        return authToken;
    }

    // Επιστρέφει τη λίστα με τα μηνύματα του χρήστη
    public List<Message> getMessageBox() {
        return messageBox;
    }

    // Προσθέτει ένα μήνυμα στη λίστα με τα μηνύματα του χρήστη
    public void addMessage(Message message) {
        this.messageBox.add(message);
    }
}
