/**
 * A Player who doesn't modify the map.
 *
 * @author JF
 * @serial exclude
 */
public class Explorer extends Player {
    /**
     * Copy details (but not inventory) from another Player.
     *
     * @param player Player to copy from
     */
    public Explorer(Player player) {
        super(player.getShort(), player.getLong(), player.getHealth());
    }

    /**
     * Create an Explorer with default health.
     * @param shortDescription A short name or description for the Explorer
     * @param longDescription A more detailed description for the Explorer
     */
    public Explorer(String shortDescription, String longDescription) {
        super(shortDescription, longDescription);
    }

    /**
     * Create an Explorer with a set health.
     * @param shortDescription A short name or description for the Explorer
     * @param longDescription A more detailed description for the Explorer
     * @param health Starting health for the Explorer.
     */
    public Explorer(String shortDescription, String longDescription, 
            int health) {
        super(shortDescription, longDescription, health);
    }

    @Override
    public String getDescription() {
        return getLong() + (isAlive()
                ? " with " + getHealth() + " health" : "(fainted)");
    }

    /**
    * @return 1
    * @inheritDoc
    */
    @Override
    public int getDamage() {
        return 1;
    }

    /**
     * Get encoded representation.
     * @return E;H;S;L where H=health, S=raw short description,
     * L=raw long description (eg "E;2;doris;There were ** chars
     * but they were replaced")
     */
    public String repr() {
        return "E;"+String.valueOf(this.getHealth())+";"
                +this.getShort()+";"+this.getLong();
    }

    /**
     * Factory to create an Explorer from a String.
     * @param encoded repr() form of the object
     * @return decoded Object or null for failure. Failures include:
     * null parameters, empty input or improperly encoded input.
     */
    public static Explorer decode(String encoded) {
        try {
            String[] parts = encoded.split(";");
            if (!parts[0].equals("E")) {
                return null;
            }
            int health = Integer.parseInt(parts[1]);
            String shortDesc = parts[2];
            String longDesc = parts[3];
            Explorer decoded = new Explorer(shortDesc, longDesc, health);
            return decoded;
        } catch (ArrayIndexOutOfBoundsException|IllegalArgumentException e) {
            return null;
        }
    }
}
