import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

/**
 * JUnit4 Test class for MapIO
 * @author Howie L.
 */
public class MapIOTest {
    /* Field */
    private Room root;
    private Room r1;
    private Room r2;
    private Builder b;
    private Critter c;
    private Explorer e;
    private Treasure t;
    private MapWalker mw;

    /**
     * Set up the rooms, exits and things. No need to test the constructor as it
     * is empty and the methods are static.
     */
    @Before
    public void setup()
        throws NullRoomException, ExitExistsException {
        this.root = new Room("r0");
        this.r1 = new Room("r1");
        this.r2 = new Room("r2");
        this.b = new Builder("bs", "bl", this.root);
        this.c = new Critter("cs", "cl", 45.677777777, 5);
        this.e = new Explorer("es", "el", 5);
        this.t = new Treasure("ts", 77.77777777);
        Room.makeExitPair(root, r1, "East", "West");
        Room.makeExitPair(r1, r2, "North", "South");
        r1.enter(c);
        r2.enter(t);
    }

    /**
     * Test with serialization.
     */
    @Test
    public void serialRoomSaveLoadTest() {
        String filename = "SRooms";
        assertTrue(MapIO.serializeMap(root, filename));
        Room rootRead = MapIO.deserializeMap(filename);
        /* Check the root room */
        assertEquals(rootRead.getExits().size(), root.getExits().size());
        assertEquals(rootRead.getContents().size(), root.getContents().size());

        mw = new MapWalker(rootRead);
        mw.walk();
        List<Room> roomsRead = mw.done;

        /* Check room 1 */
        Room r1Read = roomsRead.get(1);
        assertEquals(r1Read.getExits().size(), r1.getExits().size());
        assertEquals(r1Read.getContents().size(), r1.getContents().size());

        /* Check room 2 */
        Room r2Read = roomsRead.get(2);
        assertEquals(r2Read.getExits().size(), r2.getExits().size());
        assertEquals(r2Read.getContents().size(), r2.getContents().size());
    }

    /**
     * Test without serialization.
     */
    @Test
    public void nonSerialRoomSaveLoadTest1() {
        String filename = "NSRooms";
        assertTrue(MapIO.saveMap(root, filename));
        Object[] playerAndRoom = MapIO.loadMap(filename);
        Player player = (Player) playerAndRoom[0];
        Room rootRead = (Room) playerAndRoom[1];
        /* Check the root room */
        assertEquals(rootRead.getExits().size(), root.getExits().size());
        assertEquals(rootRead.getContents().size(), root.getContents().size());

        mw = new MapWalker(rootRead);
        mw.walk();
        List<Room> roomsRead = mw.done;

        /* Check room 1 */
        Room r1Read = roomsRead.get(1);
        assertEquals(r1Read.getExits().size(), r1.getExits().size());
        assertEquals(r1Read.getContents().size(), r1.getContents().size());

        /* Check room 2 */
        Room r2Read = roomsRead.get(2);
        assertEquals(r2Read.getExits().size(), r2.getExits().size());
        assertEquals(r2Read.getContents().size(), r2.getContents().size());
    }

    /**
     * Test abnormal conditions e.g. more than one player without serialization
     */
    @Test
    public void nonSerialRoomSaveLoadTest2() {
        String filename = "NSRooms";
        root.enter(b);
        r2.enter(e);
        assertTrue(MapIO.saveMap(root, filename));
        Object[] playerAndRoom = MapIO.loadMap(filename);
        assertNull(playerAndRoom);
        r2.leave(e);
        assertTrue(MapIO.saveMap(root, filename));
        playerAndRoom = MapIO.loadMap(filename);
        assertNotNull(playerAndRoom);
    }
}
