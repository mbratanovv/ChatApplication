package coursework;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author Mario Bratanov - 001027503
 * Mahamud Hussein - 001015542 
 * Jordan Trinder - 000978834 
 * Jack McCabe - 001034199
 */

public class CourseworkServer {

    // ID for each member
    private static int clientID;
    // an ArrarrayList<ClientThread> al;
    // instyList to keep the list of the Client
    private ArrayList<ClientThread> arrayOfThreads;
    // instance of ServerGUI 
//    private ServerGUI sg;
    // for displaying the date 
    private SimpleDateFormat dateFormat;
    // port variable for the server
    private int port;
    private boolean state; // state of the server (on/off)


    //<editor-fold defaultstate="collapsed" desc="Server constructors">
// server constructor that will receive the port for connection in the console
    public CourseworkServer(int port) {
        this.port = port;
        dateFormat = new SimpleDateFormat("HH:mm:ss"); 
        arrayOfThreads = new ArrayList<>(); 
    }
    
    // server constructor with two parameters : port and the user interface
//    public Server(int port, ServerGUI sg) {
//        this.port = port;
//        
//    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="start Method">
    public void start() {
        state = true;
        try {
            ServerSocket serverSocket = new ServerSocket(port); // new server with port
            while (state) { // waiting for connection
                display("The chat is open and waiting for its members on port: " + port + "."); // opening message
                Socket socket = serverSocket.accept(); // accept connection
                if (!state) { // if server stopped - break from the loop and continue down
                    break;
                }
                ClientThread clientThreadInstance = new ClientThread(socket);  // new thread of it
                arrayOfThreads.add(clientThreadInstance); // save it in the Array
                clientThreadInstance.start(); // run the client 
            }
            try { // to close the server
                serverSocket.close(); // close the server
                for (int i = 0; i < arrayOfThreads.size(); ++i) {
                    ClientThread tc = arrayOfThreads.get(i); // make a thread out of all members
                    try {
                        // close both streams and the socket
                        tc.input.close();
                        tc.output.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // add a message here!
                    }
                }
            } catch (Exception e) {
                display("The chat had to be closed due to unforseen exception: " + e);
            }
        } catch (IOException e) {
            String msg = dateFormat.format(new Date()) + " The chat was closed due to exception: " + e + "\n";
            display(msg);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="stop Method">
    protected void stop() { //when the user interface stops
        state = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            Socket socket = new Socket("localhost", port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="display Method">
    private void display(String message) { // method to display all the event messages
        String fullmessage = dateFormat.format(new Date()) + " " + message;
//        if (sg == null) {
            System.out.println(fullmessage);
//        } else {
//            sg.appendEvent(time + "\n");
//        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="broadcast Method">
    private synchronized void broadcast(String message) { // to send each message to all clients
        String time = dateFormat.format(new Date());
        String messageFinal = time + " " + message + "\n"; // actual message sent + the date
        // if the gui is not present - write the message in the console, else append the message to all the text boxes in the user's windows
//        if (sg == null) {
            System.out.print(messageFinal);
//        } else {
//            sg.appendRoom(messageLf);
//        }
        for (int i = arrayOfThreads.size(); --i >= 0;) {// reverse loop to check if we need to remove a disconnected user
            ClientThread clientThreadInstance = arrayOfThreads.get(i);
            if (!clientThreadInstance.writeMsg(messageFinal)) { // try to "ping" the user - if he can't get the message - delete him from the list of users
                arrayOfThreads.remove(i);
                display("The user [" + clientThreadInstance.username + "]" + " is disconnected and has been removed from the user list.");
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="remove Method">
    synchronized void remove(int ID) { // to remove a user from the userlist (arraylist)
        for (int i = 0; i < arrayOfThreads.size(); ++i) { // scan the array list until we found the Id
            ClientThread clientThreadInstance = arrayOfThreads.get(i);
            if (clientThreadInstance.id == ID) {
                arrayOfThreads.remove(i);
                return;
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="main Method">
    public static void main(String[] args) {
        int portNumber = 7777;
        CourseworkServer server = new CourseworkServer(portNumber);
        server.start();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ClientThread Class">
    class ClientThread extends Thread { // make every user instance of this class as a thread

        Socket socket;  // the socket which listens to the input/output streams
        ObjectInputStream input;
        ObjectOutputStream output;
        int id; // my unique id (easier for deconnection)
        String username; // the username of the client
        ConsoleMessage consoleMessage; // the only type of message each user will send will send
        String date; // the date I connect

        //<editor-fold defaultstate="collapsed" desc="Constructor for ClientThread class">
        ClientThread(Socket socket) { // Constructor for the class
            id = ++clientID;
            this.socket = socket;
            try {
                // creating both streams of data (input/output)
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                username = (String) input.readObject(); // read the username
                display(username + " has just connected to the chat.");
            } catch (IOException e) {
                display("Exception creating new data streams: " + e);
                return;
            } catch (ClassNotFoundException e) {
                display("The class can't be found: " + e);
            }
            date = new Date().toString() + "\n";
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="run Method">
        @Override
        public void run() { // every thread needs a run method
            boolean state = true;
            while (state) { // while loop to run while boolean = true
                try { // try to read the message which is an object of string
                    consoleMessage = (ConsoleMessage) input.readObject();
                } catch (IOException IOE) {
                    display(username + "Exception reading data streams: " + IOE);
                    break;
                } catch (ClassNotFoundException CNFE) {
                    break;
                }
                String message = consoleMessage.getMessage();

                // Switch on the type of message receive
                switch (consoleMessage.getType()) {
                    case ConsoleMessage.stringMessage:
                        broadcast(username + ": " + message);
                        break;
                    case ConsoleMessage.logout:
                        display(username + " disconnected from the chat.");
                        state = false;
                        break;
                    case ConsoleMessage.online:
                        writeMsg("List of all users currently online at " + "[" + dateFormat.format(new Date()) + "]" + "\n");
                        // scan al the users connected
                        for (int i = 0; i < arrayOfThreads.size(); ++i) {
                            ClientThread ct = arrayOfThreads.get(i);
                            writeMsg((i + 1) + ". " + ct.username + " - " + "logged in at: " + ct.date);
                        }
                        break;
                }
            }
            remove(id);
            close();
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="close Method">
        private void close() { // closing both streams and socket
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
            };
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="writeMsg Method">
        private boolean writeMsg(String message) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            try { // try sending message to the outputstream
                output.writeObject(message);
            } catch (IOException e) { // if impossible - display error message
                display("There is an error trying to communicate with " + username);
                display(e.toString());
            }
            return true;
        }
//</editor-fold>
    }
//</editor-fold>
}
