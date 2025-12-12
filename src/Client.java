import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

// Πρόγραμμα πελάτη(Client) που επικοινωνεί με τον εξυπηρετητή(Server) ανταλλαγής μηνυμάτων
public class Client {
    public static void main(String[] args) {
        // Έλεγχος αν δόθηκαν τα κατάλληλα arguments
        if (args.length < 3) {
            System.out.println("Usage: java client <ip> <port> <FN_ID> <args>");
            return;
        }

        try {
            String ip = args[0];    // IP του εξυπηρετητή
            int port = Integer.parseInt(args[1]);  // port του εξυπηρετητή
            int fnId = Integer.parseInt(args[2]);  // Λειτουργία που θα εκτελεστεί ανάλογα με το FN_ID

            // Σύνδεση με το RMI Registry και εύρεση του εξυπηρετητή
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ServerInterface server = (ServerInterface) registry.lookup("Server");

            // Ανάλογα με το FN_ID, καλείται η αντίστοιχη λειτουργία
            switch (fnId) {
                case 1: // Δημιουργία λογαριασμού όταν το FN_ID είναι 1
                    String username = args[3];
                    int token = server.createAccount(username);
                    if (token == -1) {
                        System.out.println("Invalid Username");
                    } else if (token == -2) {
                        System.out.println("Sorry, the user already exists");
                    } else {
                        System.out.println(token);
                    }
                    break;
                case 2: // Εμφάνιση όλων των λογαριασμών όταν το FN_ID είναι 2
                    int authToken = Integer.parseInt(args[3]);

                    List<String> accountsList = server.showAccounts(authToken);
                    int i = 0;
                    for (String str : accountsList)
                    {
                        System.out.println(i+1 + ". " + str);
                        i++;
                    }
                    break;
                case 3: // Αποστολή μηνύματος όταν το FN_ID είναι 3
                    authToken = Integer.parseInt(args[3]);
                    String recipient = args[4];
                    String messageBody = args[5];
                    System.out.println(server.sendMessage(authToken, recipient, messageBody));
                    break;
                case 4: // Εμφάνιση εισερχομένων μηνυμάτων (inbox) όταν το FN_ID είναι 4
                    authToken = Integer.parseInt(args[3]);
                    List<String> messageList = server.showInbox(authToken);
                    int j = 0;
                    for (String str : messageList)
                    {
                        System.out.println(j + ". " + str);
                        j++;
                    }
                    break;
                case 5: // Ανάγνωση μηνύματος όταν το FN_ID είναι 5
                    authToken = Integer.parseInt(args[3]);
                    int messageId = Integer.parseInt(args[4]);
                    System.out.println(server.readMessage(authToken, messageId));
                    break;
                case 6: // Διαγραφή μηνύματος όταν το FN_ID είναι 6
                    authToken = Integer.parseInt(args[3]);
                    messageId = Integer.parseInt(args[4]);
                    System.out.println(server.deleteMessage(authToken, messageId));
                    break;
                default:
                    System.out.println("Invalid function ID"); // Αν το FN_ID είναι δεν υπάρχει τότε εμφανίζεται κατάλληλο μήνυμα
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
