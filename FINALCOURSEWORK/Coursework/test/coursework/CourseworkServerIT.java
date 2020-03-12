package coursework;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CourseworkServerIT {
    
    public CourseworkServerIT() {
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
     * Test of main method, of class CourseworkServer.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        CourseworkServer.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serverStart method, of class CourseworkServer.
     */
    @Test
    public void testServerStart() {
        System.out.println("serverStart");
        CourseworkServer instance = null;
        instance.serverStart();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class CourseworkServer.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        int ID = 0;
        CourseworkServer instance = null;
        instance.remove(ID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
