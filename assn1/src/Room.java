import java.util.*;

/** Class Room: Building block for the map. Contains Things.
 *  @author Howie L.
 */
public class Room implements java.io.Serializable {
    private String descrip;
    private List<Thing> things = new ArrayList<>();
    private Map<String,Room> exits = new HashMap<>();

    /** Constructor of the class Room.
     * @param desc Description for the room. Note: any newlines in desc will be
     * replaced with *
     */
    public Room(String desc) {
        setDescription(desc);
    }

    /** Get the description of the room.
     * @return Current description
     */
    public String getDescription() {
        return this.descrip;
    }

    /** Change the room description. Note: any newlines in s will be replaced
     * with *
     * @param s New description to set
     */
    public void setDescription(String s) {
        String desc = s.replace("\n","*");
        this.descrip = desc;
    }

    /** Get the exits from this room.
     * @return Map of names to Rooms
     */
    public Map<java.lang.String,Room> getExits() {
        return this.exits;
    }

    /** Get the Things contained in this room.
     * @return List of Things
     */
    public List<Thing> getContents() {
        return this.things;
    }

    /** Add a new exit to this room.
     * @param name Exit name
     * @param target Room the exit goes to
     * @throws ExitExistsException If the room already has an exit of that name
     * @throws NullRoomException If target room is null
     */
    public void addExit(String name, Room target)
            throws ExitExistsException, NullRoomException {
        if (this.exits.containsKey(name)) throw new ExitExistsException();
        if (target == null) throw new NullRoomException();
        this.exits.put(name, target);
    }

    /** Remove an exit from this room. Note: silently fails if exit does
     * not exist
     * @param name Exit name
     */
    public void removeExit(String name) {
        if (this.exits.containsKey(name)) {
            this.exits.remove(name);
        }
    }

    /** Add a Thing to this room.
     * @param item Thing to add
     */
    public void enter(Thing item) {
        things.add(item);
    }

    /** Remove item from Room. Note: will fail if item is not in the Room or
     * if something wants to fight item
     * @param item Thing to remove
     * @return true if removal was successful
     */
    public boolean leave(Thing item) {
        if (item instanceof Mob) {
            /* Find any mob in the room and check whether it wants to
             * fight the item (which is probably a mob as well).
             */
            for (int i = 0; i<this.things.size(); ++i) {
                if (things.get(i) instanceof Mob) {
                    if (((Mob) things.get(i)).wantsToFight((Mob) item)) {
                        return false;
                    }
                }
            }
        }
        return things.remove(item);
    }
}
