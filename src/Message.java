import java.io.Serializable;

// Κλάση που αναπαριστά ένα μήνυμα
public class Message implements Serializable {
    private boolean isRead;  // Δείχνει αν το μήνυμα έχει διαβαστεί
    private String sender;   // Ο αποστολέας του μηνύματος
    private String receiver; // Ο παραλήπτης του μηνύματος
    private String body;     // Το κείμενο του μηνύματος

    // Κατασκευαστής για το μήνυμα
    public Message(String sender, String receiver, String body) {
        this.isRead = false;  // Το μήνυμα ξεκινάει ως μη διαβασμένο
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;
    }

    // Μέθοδος που επιστρέφει αν το μήνυμα έχει διαβαστεί
    public boolean isRead() {
        return isRead;
    }

    // Μέθοδος για να δείξουμε ότι το μήνυμα είναι διαβασμένο
    public void markAsRead() {
        this.isRead = true;
    }

    // Μέθοδος που Επιστρέφει τον αποστολέα του μηνύματος
    public String getSender() {
        return sender;
    }

    // Μέθοδος που Επιστρέφει τον παραλήπτη του μηνύματος
    public String getReceiver() {
        return receiver;
    }

    // Μέθοδος που Επιστρέφει το κείμενο του μηνύματος
    public String getBody() {
        return body;
    }
}
