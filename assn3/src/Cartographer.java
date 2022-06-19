import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import java.util.*;

/**
 * This class will be responsible for rendering the "map" in the GUI.
 * @author Howie L.
 */
public class Cartographer extends Canvas {
    /* Sketcher and constants */
    private GraphicsContext context;
    private int ROOM_WIDTH = 50;
    private int ROOM_HEIGHT = 50;
    private int EXIT_LINE_LENGTH = 7;

    /**
     * Constructor of the class.
     * @param width Canvas width
     * @param height Canvas height
     */
    public Cartographer(int width, int height) {
        super(width, height);
        context = this.getGraphicsContext2D();
        context.setFill(Color.TRANSPARENT);
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
    }

    /**
     * Draw all the rooms, exits and things.
     * @param bm Bounds mapper that stores the whole map information.
     */
    public void draw(BoundsMapper bm) {
        Map<Room, Pair> roomCoords = bm.coords;
        for (Map.Entry<Room, Pair> roomEntry : roomCoords.entrySet()) {
            Room r = roomEntry.getKey();
            Pair loc = roomEntry.getValue();
            drawRoom(bm, loc);
            drawExit(bm, r, loc);
            drawItem(bm, r, loc);
        }
    }

    /**
     * Draw a single room as a square.
     * @param bm Bounds mapper that stores the whole map information.
     * @param loc Room coordinates
     */
    private void drawRoom(BoundsMapper bm, Pair loc) {
        this.context.strokeRect((loc.x-bm.xMin)*this.ROOM_WIDTH,
                (loc.y-bm.yMin)*this.ROOM_HEIGHT,
                this.ROOM_WIDTH,
                this.ROOM_HEIGHT);
    }

    /**
     * Draw exits of a room with small strokes into the interior at the
     * midpoints of edges.
     * @param bm Bounds mapper that stores the whole map information.
     * @param r Room to get exits from
     * @param loc Room coordinates
     */
    private void drawExit(BoundsMapper bm, Room r, Pair loc) {
        Map<String, Room> exits = r.getExits();
        double pt1X, pt1Y, pt2X, pt2Y;
        for (Map.Entry<String, Room> entry : exits.entrySet()) {
            switch (entry.getKey()) {
                case "East":
                    pt1X = (loc.x-bm.xMin+1)*ROOM_WIDTH + EXIT_LINE_LENGTH/2;
                    pt1Y = (loc.y-bm.yMin+0.5)*ROOM_HEIGHT;
                    pt2X = (loc.x-bm.xMin+1)*ROOM_WIDTH - EXIT_LINE_LENGTH/2;
                    pt2Y = (loc.y-bm.yMin+0.5)*ROOM_HEIGHT;
                    break;
                case "West":
                    pt1X = (loc.x-bm.xMin)*ROOM_WIDTH + EXIT_LINE_LENGTH/2;
                    pt1Y = (loc.y-bm.yMin+0.5)*ROOM_HEIGHT;
                    pt2X = (loc.x-bm.xMin)*ROOM_WIDTH - EXIT_LINE_LENGTH/2;
                    pt2Y = (loc.y-bm.yMin+0.5)*ROOM_HEIGHT;
                    break;
                case "North":
                    pt1X = (loc.x-bm.xMin+0.5)*ROOM_WIDTH;
                    pt1Y = (loc.y-bm.yMin)*ROOM_HEIGHT - EXIT_LINE_LENGTH/2;
                    pt2X = (loc.x-bm.xMin+0.5)*ROOM_WIDTH;
                    pt2Y = (loc.y-bm.yMin)*ROOM_HEIGHT + EXIT_LINE_LENGTH/2;
                    break;
                case "South":
                    pt1X = (loc.x-bm.xMin+0.5)*ROOM_WIDTH;
                    pt1Y = (loc.y-bm.yMin+1)*ROOM_HEIGHT - EXIT_LINE_LENGTH/2;
                    pt2X = (loc.x-bm.xMin+0.5)*ROOM_WIDTH;
                    pt2Y = (loc.y-bm.yMin+1)*ROOM_HEIGHT + EXIT_LINE_LENGTH/2;
                    break;
                default:
                    pt1X = pt1Y = pt2X = pt2Y = -1;
                    break;
            }
            context.strokeLine(pt1X, pt1Y, pt2X, pt2Y);
        }
    }

    /**
     * Draw things in a room.
     * @param bm Bounds mapper that stores the whole map information.
     * @param r Room to get things from
     * @param loc Room coordinates
     */
    private void drawItem(BoundsMapper bm, Room r, Pair loc) {
        List<Thing> items = r.getContents();
        double ptX, ptY;
        for (Thing item : items) {
            if (item instanceof Player) {
                ptX = (loc.x-bm.xMin+0.125)*ROOM_WIDTH;
                ptY = (loc.y-bm.yMin+0.25)*ROOM_HEIGHT;
                context.strokeText("@", ptX, ptY);
            } else if (item instanceof Treasure) {
                ptX = (loc.x-bm.xMin+0.75)*ROOM_WIDTH;
                ptY = (loc.y-bm.yMin+0.25)*ROOM_HEIGHT;
                context.strokeText("$", ptX, ptY);
            } else if (item instanceof Critter) {
                ptX = (loc.x-bm.xMin+0.125)*ROOM_WIDTH;
                ptY = (loc.y-bm.yMin+0.875)*ROOM_HEIGHT;
                if (((Critter) item).isAlive()) {
                    context.strokeText("M", ptX, ptY);
                } else {
                    context.strokeText("m", ptX, ptY);
                }
            }
        }
    }

    /**
     * Reset the cartographer contents drawn before.
     */
    public void clear() {
        context.clearRect(0, 0, this.getWidth(), this.getHeight());
    }
}
