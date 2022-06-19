import java.util.*;

/**
 * Find the bounding box for the overall map.
 * @author Howie L.
 */
public class BoundsMapper extends MapWalker {
    /* Field */
    public Map<Room,Pair> coords;  // Map Rooms to coordinates
    public int xMax;  // Maximum x coordinate for rooms (root has x=0)
    public int xMin;  // Minimum x coordinate for rooms (root has x=0)
    public int yMax;  // Maximum y coordinate for rooms (root has y=0)
    public int yMin;  // Minimum y coordinate for rooms (root has y=0)

    /**
     * Constructor of the BoundsMapper class
     * @param root The starting room
     */
    public BoundsMapper(Room root) {
        super(root);
        coords = new HashMap<>();
        xMin = 0;
        xMax = 0;
        yMin = 0;
        yMax = 0;
    }

    /**
     * Assign room coordinates relative to a neighbour.
     * If room has no known neighbours, give it coordinate (0,0).
     * If your "North" neighbour has coordinates (x,y), then your coordinates
     * should be (x, y-1). If your "East" neighbour has coordinates (x,y),
     * then your coordinates should be (x+1, y). (Similar for South and West).
     * Check for known coordinates in order: North, South, East, West.
     * @param room Room to assign coordinates to
     */
    @Override
    protected void visit(Room room) {
        Map<String, Room> currentExits = room.getExits();
        Pair currentCoords = new Pair(0, 0);
        if (!currentExits.isEmpty()) {
            if (coords.containsKey(currentExits.get("North"))) {
                Room prevRoom = currentExits.get("North");
                Pair prevRoomCoords = coords.get(prevRoom);
                currentCoords = new Pair(prevRoomCoords.x,
                        prevRoomCoords.y - 1);
            } else if (coords.containsKey(currentExits.get("South"))){
                Room prevRoom = currentExits.get("South");
                Pair prevRoomCoords = coords.get(prevRoom);
                currentCoords = new Pair(prevRoomCoords.x,
                        prevRoomCoords.y + 1);
            } else if (coords.containsKey(currentExits.get("East"))) {
                Room prevRoom = currentExits.get("East");
                Pair prevRoomCoords = coords.get(prevRoom);
                currentCoords = new Pair(prevRoomCoords.x + 1,
                        prevRoomCoords.y);
            } else if (coords.containsKey(currentExits.get("West"))) {
                Room prevRoom = currentExits.get("West");
                Pair prevRoomCoords = coords.get(prevRoom);
                currentCoords = new Pair(prevRoomCoords.x - 1,
                        prevRoomCoords.y);
            }
        }  /* Keep (0, 0) for rooms without normal exits */
        coords.put(room, currentCoords);

        /* Update boundary coordinates */
        updateCoordsExtrema();
    }

    /**
     * Called by walk --- clears any state from previous walks. Subclasses which
     * @Override this method must call super.reset() internally to ensure that
     * parent state is cleared as well.
     */
    @Override
    public void reset() {
        super.reset();
        coords.clear();
    }

    /*
     * Return the maxima and minima of the coordinates in the Room map.
     */
    private void updateCoordsExtrema() {
        int[] extrema = new int[4]; /* {xMin, xMax, yMin, yMax} */
        Map.Entry<Room, Pair> maxXEntry = null;
        Map.Entry<Room, Pair> minXEntry = null;
        Map.Entry<Room, Pair> maxYEntry = null;
        Map.Entry<Room, Pair> minYEntry = null;

        for (Map.Entry<Room, Pair> entry : this.coords.entrySet()) {
            /* Non-empty map */
            Pair coord = entry.getValue();
            if (coord != null) {
                if (minXEntry == null || Integer.compare(coord.x,
                        minXEntry.getValue().x) < 0) {
                    minXEntry = entry;
                }
                if (maxXEntry == null || Integer.compare(coord.x,
                        maxXEntry.getValue().x) > 0) {
                    maxXEntry = entry;
                }
                if (minYEntry == null || Integer.compare(coord.y,
                        minYEntry.getValue().y) < 0) {
                    minYEntry = entry;
                }
                if (maxYEntry == null || Integer.compare(coord.y,
                        maxYEntry.getValue().y) > 0) {
                    maxYEntry = entry;
                }
            }
        }

        try {
            this.xMin = minXEntry.getValue().x;
            this.xMax = maxXEntry.getValue().x;
            this.yMin = minYEntry.getValue().y;
            this.yMax = maxYEntry.getValue().y;
        } catch (NullPointerException npe) {
            /* In case coords is empty, yet the extrema are by default
             * zero when declared. No need to assign values.
             */
            return;
        }
    }
}
