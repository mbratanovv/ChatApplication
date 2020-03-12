package coursework;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class CourseworkClientIT {
    
    // read/write from the socket
    private ObjectInputStream input;		
    private ObjectOutputStream output;		
    private Socket socket;
    private String server, username; // the server, the port and the username
    private int port;
    
    public CourseworkClientIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of main method, of class CourseworkClient.
     */
    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        int portNumber = 6666;
        CourseworkClient.main(args);
        CourseworkClient client = new CourseworkClient(portNumber);
    }
    
    @Test
    public void testClient(){
        CourseworkClient client = new CourseworkClient();
        AssertTrue(client.clientStart());
    }
    
    /**
     * Test of clientStart method, of class CourseworkClient.
     */
    @Ignore
    @Test
    public void testClientStart() throws Exception {
        System.out.println("clientStart");
        CourseworkClient instance = null;
        boolean expResult = false;
        boolean result = instance.clientStart();
        assertEquals(expResult, result);
    }

    /**
     * Test of sendMessageToServer method, of class CourseworkClient.
     */
    @Ignore
    @Test
    public void testSendMessageToServer() {
        System.out.println("sendMessageToServer");
        ConsoleMessage message = null;
        CourseworkClient instance = null;
        instance.sendMessageToServer(message);
        assertNull(message);
    }

    /**
     * Test of outputMessage method, of class CourseworkClient.
     */
    @Ignore
    @Test
    public void testOutputMessage() {
        System.out.println("outputMessage");
        String message = "";
        CourseworkClient instance = null;
        instance.outputMessage(message);
    }
    
}
