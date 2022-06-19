import java.io.*;
import java.util.*;

/**
 * Static routines to save and load Rooms
 * @author Howie L.
 */
public class MapIO {
    /**
     * Constructor for the MapIO class
     */
    public MapIO() {}

    /**
     * Write rooms to a new file (using Java serialisation).
     * @param root Start room to explore from
     * @param filename Filename to write to
     * @return true if successful
     */
    public static boolean serializeMap(Room root, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(root);
            oos.close();
            fos.close();
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    /**
     * Read serialised Rooms from a file.
     * @param filename Filename to read Rooms from
     * @return start Room or null on failure
     */
    public static Room deserializeMap(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Room startRoom = (Room) ois.readObject();
            ois.close();
            fis.close();
            return startRoom;
        } catch (IOException|ClassNotFoundException exc) {
            return null;
        }
    }

    /**
     * Write Rooms to a new file (using encoded String form).
     * @param root Start room to explore from
     * @param filename Filename to write to
     * @return true if successful
     */
    public static boolean saveMap(Room root, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            PrintWriter pw = new PrintWriter(fos);
            MapWalker mw = new MapWalker(root);
            mw.walk();
            List<Room> roomList = mw.done;

            /* Print the number of rooms and their descriptions */
            pw.println(roomList.size());
            for (Room roomWalked:roomList) {
                pw.println(roomWalked.getDescription());
            }

            /* Print the exit information for each room */
            for (Room roomWalked:roomList) {
                Map<String, Room> roomExit = roomWalked.getExits();
                pw.println(roomExit.size());
                for (Map.Entry<String, Room> entry : roomExit.entrySet()) {
                    String key = entry.getKey().toString();
                    int roomId = roomList.indexOf(entry.getValue());
                    pw.println(roomId+" "+key);
                }
            }

            /* Print the content information for each room */
            for (Room roomWalked:roomList) {
                List<Thing> roomContents = roomWalked.getContents();
                if (!roomContents.isEmpty()) {
                    pw.println(roomContents.size());
                    for (Thing item : roomContents) {
                        pw.println(item.repr());
                    }
                } else {
                    pw.println(0);
                }
                /* Won't print if no contents */
            }

            pw.close();
            fos.close();
        } catch (IOException ioe) {
            return false;
        }
        return true;
    }

    /**
     * Decode a String into a Thing. (Need to be able to decode Treasure,
     * Critter, Builder and Explorer)
     * @param encoded String to decode
     * @param root Start room for the map
     * @return Decoded Thing or null on failure (null arguments or
     * incorrectly encoded input).
     */
    public static Thing decodeThing(String encoded, Room root) {
        Thing thing;
        switch (encoded.charAt(0)) {
            case 'B':
                thing = Builder.decode(encoded, root);
                break;
            case 'C':
                thing = Critter.decode(encoded);
                break;
            case 'E':
                thing = Explorer.decode(encoded);
                break;
            case '$':
                thing = Treasure.decode(encoded);
                break;
            default:
                thing = null;
                break;
        }
        return thing;
    }

    /**
     * Read information from a file created with saveMap().
     * @param filename Filename to read from
     * @return null if unsucessful. If successful, an array of two Objects.
     * [0] being the Player object (if found) and [1] being the start room.
     */
    public static Object[] loadMap(String filename) {
        Object[] playerAndRoom = new Object[2];
        List<Room> roomRead = new ArrayList<>();
        try {
            int playerCount = 0;
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            try {
                int roomCount = Integer.parseInt(br.readLine());
                int roomIdx;
                for (roomIdx = 0; roomIdx < roomCount; ++roomIdx) {
                    String roomDesc = br.readLine();
                    if (roomDesc == null) {
                        return null;
                    }
                    Room r = new Room(roomDesc);
                    roomRead.add(r);
                    if (roomIdx == 0) {
                        playerAndRoom[1] = r;
                    }
                }

                /* Try reading the exit info */
                int exitIdx;
                for (roomIdx = 0; roomIdx < roomCount; ++roomIdx) {
                    int exitCount = Integer.parseInt(br.readLine());
                    Room currentRoom = roomRead.get(roomIdx);
                    for (exitIdx = 0; exitIdx < exitCount; ++exitIdx) {
                        String[] exitInfo = br.readLine().split(" ");
                        currentRoom.addExit(exitInfo[1],
                                roomRead.get(Integer.parseInt(exitInfo[0])));
                    }
                }

                /* Try reading the thing info */
                int thingIdx;
                for (roomIdx = 0; roomIdx < roomCount; ++roomIdx) {
                    int thingCount = Integer.parseInt(br.readLine());
                    Room currentRoom = roomRead.get(roomIdx);
                    for (thingIdx = 0; thingIdx < thingCount; ++thingIdx) {
                        String encoded = br.readLine();
                        Thing thing = decodeThing(encoded,
                                (Room) playerAndRoom[1]);
                        if (thing == null) {
                            return null;
                        }
                        currentRoom.enter(thing);
                        if (thing instanceof Explorer ||
                                thing instanceof Builder) {
                            playerAndRoom[0] = thing;
                            playerCount++;
                        }
                        if (playerCount > 1) {
                            /* More than one player */
                            return null;
                        }
                    }
                }

                fr.close();
                br.close();
                return playerAndRoom;
            } catch (IllegalArgumentException|
                    ExitExistsException|
                    NullRoomException|
                    NullPointerException e) {
                return null;  /* Invalid info */
            }
        } catch (IOException ioe) {
            return null;  /* Bad access to the file */
        }
    }
}
