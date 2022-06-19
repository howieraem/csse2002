import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/** Class RoomTest: JUnit4 Testing Class for Class Room
 * @author Howie L.
 */
public class RoomTest {
    /** Method roomDescriptionTest1(): Test the constructor, getDescription()
     * and setDescription() with strings
     */
    @Test
    public void roomDescriptionTest1() {
        Room room1 = new Room("r1");
        assertEquals("r1", room1.getDescription());
        room1.setDescription("");
        assertEquals("", room1.getDescription());
    }

    /** Method roomDescriptionTest2(): Test the constructor and getDescription()
     * with a null string
     */
    @Test
    public void roomDescriptionTest2() {
        Room room1 = new Room(null);
        assertEquals(null, room1.getDescription());
    }

    /** Method roomContentsTest1(): Test the content methods with some Things
     */
    @Test
    public void roomContentsTest1() {
        Room room1 = new Room("r1");
        Thing it1 = new Thing("sd1", "ld1");
        Mob it2 = new Critter("", "", 0, -1);
        Mob it3 = new Explorer("", "ld2");
        Treasure it4 = new Treasure("tr", 2.13);
        /* Test if the list is working */
        List<Thing> test_contents = new ArrayList<>();
        room1.enter(it1);
        test_contents.add(it1);
        assertEquals(room1.getContents(), test_contents);
        /* A dead critter may prevent an explorer from leaving, yet it is
         * lootable by the explorer.
         */
        room1.enter((Thing) it3);
        room1.enter((Thing) it2);
        assertFalse(room1.leave((Thing) it3));
        assertTrue(room1.leave((Thing) it2));
        assertTrue(room1.leave(it1));
        /* Test a treasure */
        room1.enter(it4);
        assertTrue(room1.leave(it4));
    }

    /** Method roomContentsTest2(): Test the content methods with an abnormal
     * Thing
     */
    @Test
    public void roomContentsTest2() {
        Room room1 = new Room("r1");
        Thing it1 = new Thing("sd1", null);
        room1.enter(it1);
        assertTrue(room1.leave(it1));
    }

    /** Method roomExitTest1(): Test the exit methods with some exit
     * names and rooms
     */
    @Test
    public void roomExitTest1() throws ExitExistsException, NullRoomException {
        /* Exits, with null names and pointing to the same room, will pass. */
        Room room1 = new Room("");
        room1.addExit(null, room1);
        room1.removeExit(null);
        room1.removeExit("e1");  /* Silent fail */
    }

    /** Method roomExitTest2(): Test the exit methods with some exit
     * names and rooms
     */
    @Test
    public void roomExitTest2() throws ExitExistsException, NullRoomException {
        Room room1 = new Room("");
        room1.addExit("e1", null);
        room1.removeExit("e1");
    }

    /** Method roomExitTest3(): Test the exit methods with same exit names
     */
    @Test
    public void roomExitTest3() throws ExitExistsException, NullRoomException {
        Room room1 = new Room("r1");
        Room room2 = new Room("");
        room1.addExit("e1", room1);
        room1.addExit("e1", room2);
    }

    /** Method roomExitTest4(): Test the exit methods and exception handling
     * with some exit names and rooms
     */
    @Test
    public void roomExitTest4() {
        Room room1 = new Room("r1");
        Room room2 = new Room("r2");
        Room room3 = null;
        Map<String, Room> test_exits = new HashMap<>();
        test_exits.put("e1", room1);
        test_exits.put("e2", room3);
        try {
            room1.addExit("e1", room2);
            room1.addExit("e2", room3);
            room1.addExit("e1", room1);
            /* Test if the map is working */
            assertEquals(test_exits, room1.getExits());
            fail();
        } catch (ExitExistsException eee) { /* Squashing */
        } catch (NullRoomException nre) { /* Squashing */
        }
    }
}
