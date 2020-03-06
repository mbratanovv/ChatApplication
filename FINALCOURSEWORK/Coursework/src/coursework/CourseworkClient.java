package coursework;

import java.net.*;
import java.io.*;
import java.util.*;


public class CourseworkClient {

    // read/write from the socket
    private ObjectInputStream input;		
    private ObjectOutputStream output;		
    private Socket socket;
    private String server, username; // the server, the port and the username
    private int port;

    CourseworkClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    //<editor-fold defaultstate="collapsed" desc="start Method">
    public boolean start() {
        try {
            socket = new Socket(server, port);
        }
        catch (Exception ex) {
            outputMessage("There is an error connectiong to server: " + ex);
            return false; // couldn't connect to the server
        } // getInetAddress returns the remote IP address to which the socket is connected, or null if it is not connected
        String msg = "Connection accepted at: " + "address[" + socket.getInetAddress() + "], " + "on port[" + socket.getPort() + "]"; 
        outputMessage(msg);
        
        try { //create both I/O streams
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eio) {
            outputMessage("Exception in creating new Input/Output streams: " + eio);
            return false; // couldn't create them
        }
        //<editor-fold defaultstate="collapsed" desc="Login process">
        new CourseworkServerListenerThread().start(); // creates the Thread to listen from the server from the ListenFromServer class
        try {
            output.writeObject(username); // sending our username to the server as a String
        } catch (IOException eio) {
            outputMessage("Exception while logging in: " + eio);
            disconnect();
            return false; // couldn't login
        }
//</editor-fold>
        return true; // successfull
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="outputMessage Method">
    private void outputMessage(String message) { // to output a message in the console
            System.out.println(message);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="sendMessage Method">
    void sendMessage(ConsoleMessage message) { // To send a message to the server
        try {
            output.writeObject(message);
        } catch (IOException e) {
            outputMessage("Exception occured when writing to the server: " + e);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="disconnect Method">
    private void disconnect() { // method to close both streams and disconnect in something occurs
        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
        }
        try {
            if (output != null) {
                output.close();
            }
        } catch (Exception e) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
    }
//</editor-fold>

    public static void main(String[] args) {
        // M.B - to add details for login : port number of the server and automatically display details of each member that logs in
        // the default IP and port Number of the server will be displayed when the server is started : 
        // the welcoming message is already there, but must add InetAddress functionality and to display both default IP insted of localhost and portnumber
        int portNumber = 7777;
        String serverAddress = "localhost";
        String userName = "Anonymous";
        CourseworkClient client = new CourseworkClient(serverAddress, portNumber, userName);
        if (!client.start()) { 
            return;
        }

        // M.B - change from scanner to bufferedreader to read the input messages
        Scanner scanner = new Scanner(System.in); // wait for messages from user
        //BufferedReader bufferedReader = new BufferedReader(input);
        //<editor-fold defaultstate="collapsed" desc="to change this loop (update)">
        while (true) { // loop to determine the messages coming from each user
            System.out.print("> ");
            String message = scanner.nextLine();
            // M.B - to change the statement to switch/case or change the if statements and add more (empty string)
            if (message.equalsIgnoreCase("logoutMessage")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.logout, ""));
                break;
            }
            else if (message.equalsIgnoreCase("online")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.online, ""));
            } else {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.stringMessage, message));
            }
        }
//</editor-fold>
        client.disconnect(); // to disconnect when the break statement is triggered (when a client wants to logout)
    }

    class CourseworkServerListenerThread extends Thread { // M.B - decide if to keep the class or no : class used to print messages from server to clients ?

        @Override
        public void run() {
            while (true) {
                try {
                    String message = (String) input.readObject();
                        System.out.println(message);
                        System.out.print("> ");
                } catch (IOException ioe) {
                    outputMessage("You have logged out of the server successfully with the exception: " + ioe);
                    break;
                } 
                catch (ClassNotFoundException cnfe) {
                }
            }
        }
    }
}
