import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * JUnit4 Test class for the BoundsMapper
 * @author Howie L.
 */
public class BoundsMapperTest {
    /* Field */
    private Room root;
    private Room r1;
    private Room r2;
    private Room r3;
    private Room r4;
    private Room r5;

    /**
     * Set up the rooms.
     */
    @Before
    public void setup() {
        this.root = new Room("r0");
        this.r1 = new Room("r1");
        this.r2 = new Room("r2");
        this.r3 = new Room("");
        this.r4 = new Room(" ");
        this.r5 = new Room("r5");
    }

    /**
     * Test method for the constructor and the simple one room case.
     */
    @Test
    public void bmConstructorTest() {
        BoundsMapper bm = new BoundsMapper(root);
        bm.walk();
        assertTrue(bm.hasVisited(root));
        assertEquals(bm.xMin, 0);
        assertEquals(bm.xMax, 0);
        assertEquals(bm.yMin, 0);
        assertEquals(bm.yMax, 0);
    }

    /**
     * Test rooms with normal exit pairs added.
     * Room locations as follows:
     *
     * r4 ⇄ r1 ⇄ r2 ⇄ r5
     *       ⇅
     *      root
     *       ⇅
     *       r3
     *
     */
    @Test
    public void bmTest1()
            throws ExitExistsException,NullRoomException {
        Room.makeExitPair(root, r1, "North", "South");
        Room.makeExitPair(r1, r2, "East", "West");
        Room.makeExitPair(root, r3, "South", "North");
        Room.makeExitPair(r1, r4, "West", "East");
        Room.makeExitPair(r2, r5, "East", "West");
        BoundsMapper bm = new BoundsMapper(root);
        bm.walk();
        assertTrue(bm.hasVisited(root));
        assertTrue(bm.hasVisited(r1));
        assertTrue(bm.hasVisited(r2));
        assertTrue(bm.hasVisited(r3));
        assertTrue(bm.hasVisited(r4));
        assertTrue(bm.hasVisited(r5));
        assertEquals(bm.coords.size(), 6);
        assertEquals(bm.xMin, -2);
        assertEquals(bm.xMax, 1);
        assertEquals(bm.yMin, -1);
        assertEquals(bm.yMax, 1);
    }

    /**
     * Test abnormal room configurations, such as
     * abnormal exit name, one-way exit and room "loop".
     *
     *  r5
     *
     *  r1  ⇄  r2 → r3
     *  ⇅      ⇅
     * root ⇄  r4
     *
     */
    @Test
    public void bmTest2()
            throws ExitExistsException, NullRoomException {
        Room.makeExitPair(root, r1, "North", "South");
        Room.makeExitPair(r1, r2, "East", "West");
        Room.makeExitPair(root, r4, "East", "West");
        Room.makeExitPair(r4, r2, "North", "South");
        r2.addExit("East", r3);
        Room.makeExitPair(r1, r5, "N", "S");
        BoundsMapper bm = new BoundsMapper(root);
        bm.walk();
        assertTrue(bm.hasVisited(root));
        assertTrue(bm.hasVisited(r1));
        assertTrue(bm.hasVisited(r2));
        assertTrue(bm.hasVisited(r3));
        assertTrue(bm.hasVisited(r4));
        assertTrue(bm.hasVisited(r5));
        assertEquals(bm.coords.size(), 6);
        assertEquals(bm.xMin, -1);
        assertEquals(bm.xMax, 0);
        assertEquals(bm.yMin, 0);
        assertEquals(bm.yMax, 1);
        Pair origin = new Pair(0,0);
        assertEquals(bm.coords.get(r3), origin);
        assertEquals(bm.coords.get(r5), origin);
    }
}
