import java.util.*;

/**
 * Iterator over all reachable Rooms
 * @author Howie L.
 */
public class MapWalker {
    /* Field */
    private Room start;
    public List<Room> todo;
    public List<Room> done;

    /**
     * Constructor of the MapWalker class. Choose start room but Do not start
     * the walk process.
     * @param start Room to begin exploring from
     */
    public MapWalker(Room start) {
        this.start = start;
        todo = new LinkedList<>();
        done = new LinkedList<>();
    }

    /**
     * Called by walk --- clears any state from previous walks. Subclasses which
     * @Override this method must call super.reset() internally to ensure that
     * parent state is cleared as well.
     */
    protected void reset() {
        todo.clear();
        done.clear();
    }

    /**
     * Visit all reachable rooms and call visit(). Note that code related to
     * tracking which rooms have been visited should be included here and not
     * in visit(). Call reset() at the beginning of this routine.
     */
    public void walk() {
        reset();
        todo.add(this.start);
        Room r;
        while (!todo.isEmpty()) {
            r = todo.get(0);
            todo.remove(r);
            if (!done.contains(r)) {
                Map<String, Room> currentExits = r.getExits();
                for (String key: currentExits.keySet()) {
                    todo.add(currentExits.get(key));
                }
                visit(r);
                done.add(r);
            }
        }
    }

    /**
     * @param room Room to query
     * @return true if room has been processed
     */
    public boolean hasVisited(Room room) {
        return done.contains(room);
    }

    /**
     * Process a room override to customise behaviour. For this parent class,
     * it is an empty method.
     * @param room Room to deal with
     */
    protected void visit(Room room) {
    }
}
