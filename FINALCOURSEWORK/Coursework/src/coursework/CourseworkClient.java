package coursework;

import java.net.*;
import java.io.*;
import java.util.*;


public class CourseworkClient  {

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
    
    //<editor-fold defaultstate="collapsed" desc="main Method">
    public static void main(String[] args) throws IOException {
        int portNumber = 7777;
        try {
            InetAddress IPAddress = InetAddress.getLocalHost();
            System.out.println("The default IP Address for this server is: " + "[" + IPAddress + "]");
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot identify local host address" + uhe);
        }
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter your username: ");
        String[] consoleValues = bufferedReader.readLine().split(" ");
        String IPAddress = null;
        CourseworkClient client = new CourseworkClient(IPAddress, portNumber, consoleValues[0]);
        if (!client.clientStart()) {
            return;
        }
        System.out.println("To view the command list for this network type: 'commandlist' ");
        Scanner scanner = new Scanner(System.in); // wait for messages from user
        while (true) { // loop to determine the messages coming from each user
            System.out.print("> ");
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("logout")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.logout, ""));
                break;
            }else if (message.equalsIgnoreCase("online")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.online, ""));
            }else if(message.equalsIgnoreCase("commandlist")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.commands, ""));
            }else if(message.equalsIgnoreCase("coordinator")) {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.coordinator, ""));
            }else {
                client.sendMessage(new ConsoleMessage(ConsoleMessage.stringMessage, message));
            }
        }
        client.disconnect(); // to disconnect when the break statement is triggered (when a client wants to logout)
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="start Method">
    public boolean clientStart() throws UnknownHostException {
        try {
            socket = new Socket(server, port);
        }
        catch (Exception ex) {
            outputMessage("There is an error connectiong to server: " + ex);
            return false; 
        }
        String msg = "Connection accepted on port: " + "[" + socket.getPort() + "]"; 
        outputMessage(msg);
        
        try { //create both I/O streams
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eio) {
            outputMessage("Exception in creating new Input/Output streams: " + eio);
            return false; 
        }
        //<editor-fold defaultstate="collapsed" desc="Login process">
        new CourseworkServerThread().start(); // creates the Thread to listen from the server from the ListenFromServer class
        try {
            output.writeObject(username); 
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
    void outputMessage(String message) { // to output a message in the console
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

    class CourseworkServerThread extends Thread { // M.B - decide if to keep the class or no : class used to print messages from server to clients ?

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
