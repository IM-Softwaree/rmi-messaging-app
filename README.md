The implemented classes are five in total:

1. **Message.java**
    - This class represents a message in the system.
    - It has 4 properties:
        - `boolean isRead`: Indicates whether the message has been read or not.
        - `String sender`: The username of the sender.
        - `String receiver`: The username of the receiver.
        - `String body`: The content of the message.
      
    - It also has the following methods (in addition to the constructor):
        - `boolean isRead()`: Returns whether the message has been read.
        - `void markAsRead()`: Marks the message as read.
        - `String getSender()`: Returns the sender of the message.
        - `String getReceiver()`: Returns the receiver of the message.
        - `String getBody()`: Returns the text of the message.

2. **Account.java**
    - This class represents a user account in the system.
    - It has 3 properties:
        - `String username`: The unique username of the account.
        - `int authToken`: A unique token assigned by the server to authenticate the user.
        - `List<Message> messageBox`: A list of messages that have been sent to this account.
      
    - It also has the following methods (in addition to the constructor):
        - `String getUsername()`: Returns the username.
        - `int getAuthToken()`: Returns the unique authentication token for the user.
        - `List<Message> getMessageBox()`: Returns the list of messages of the user.
        - `void addMessage(Message message)`: Adds a message to the user’s message list.

3. **ServerInterface.java**
    - This interface defines the RMI (Remote Method Invocation) operations that the server exposes to its clients.
   
    - It declares 6 methods:
        - `int createAccount(...)`: Creates a new account and returns a unique auth token.
        - `List<String> showAccounts(...)`: Returns a list of all usernames in the system.
        - `String sendMessage(...)`: Sends a message to a recipient.
        - `List<String> showInbox(...)`: Returns all messages for a specific user.
        - `String readMessage(...)`: Reads a specific message and marks it as read.
        - `String deleteMessage(...)`: Deletes a specific message.

4. **Server.java**
    - This class implements the `ServerInterface`.
    - It manages user accounts and messages.
    - It uses a `HashMap` to store accounts, where the key is the username (`String`) and the value is the corresponding `Account` object.
    - Through this class the system can handle multiple user requests concurrently.
    - The server assigns a unique `authToken` to each user when an account is created, and this token is required for the user’s actions.
    - All usernames must be alphanumeric or contain the underscore (`_`). Invalid usernames are rejected (this is checked at the beginning of `createAccount(String username)`).
    - The system does not persist data between executions: all data (accounts, messages) are stored in memory and are lost when the server stops.
    - The server supports concurrent requests from multiple clients using synchronization.
    
    - It has 2 main fields:
        - `Map<String, Account> accounts`: A map that stores usernames and maps them to their corresponding account.
        - `int authTokenCounter`: A counter used to generate unique auth tokens. It starts from 1000.

5. **Client.java**
    - This class allows users to interact with the server by sending requests and receiving responses (depending on the `FN_ID` provided).
    - The supported operations are:
        - Create account (`FN_ID == 1`)
        - Show all accounts in the system (`FN_ID == 2`)
        - Send a message to another user (`FN_ID == 3`)
        - Show the inbox (unread messages are marked with a star `*`, read messages without a star) (`FN_ID == 4`)
        - Read a message (`FN_ID == 5`)
        - Delete a message (`FN_ID == 6`)

6. Each message has an **ID** (the ID is essentially its position in the user’s message list, e.g. the first message has ID 0, the second has ID 1, etc.).  
   This ID is used when reading and deleting specific messages.

7. **How to run the program**

   1) First, run the server class (`Server`) and pass the arguments from the command line.  
      The server expects a single integer argument, which is the `<port number>`.

   2) Then run the client class (`Client`) with the appropriate arguments.  
      The format of the arguments depends on the operation you want to perform:

      - **Examples:**
        
        - Create a user:  
          `localhost 5000 1 tester`  
          Command format:  
          `<ip> <port number> 1 <username>`  
          Here the user `"tester"` will receive `authToken = 1000`.  
          If we create another user:  
          `localhost 5000 1 tester2`  
          then `tester2` will have `authToken = 1001`.

        - Show all accounts in the system:  
          `localhost 5000 2 1000`  
          Command format:  
          `<ip> <port number> 2 <authToken>`.

        - Send a message `<message_body>` to the account with username `<recipient>`:  
          `localhost 5000 3 1000 tester2 "HELLO WORLD"`  
          Command format:  
          `<ip> <port number> 3 <authToken> <recipient> <message_body>`.

        - Show the inbox (all messages) for a specific user:  
          `localhost 5000 4 1001`  
          Command format:  
          `<ip> <port number> 4 <authToken>`.

        - Read the content of a message with id `<message_id>` for a specific user and mark it as read:  
          `localhost 5000 5 1001 0`  
          Command format:  
          `<ip> <port number> 5 <authToken> <message_id>`.

        - Delete the message with id `<message_id>`:  
          `localhost 5000 6 1001 0`  
          Command format:  
          `<ip> <port number> 6 <authToken> <message_id>`.
