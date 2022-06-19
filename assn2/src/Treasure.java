/**
 * Lootable object which doesn't fight.
 *
 * @author JF
 * @serial exclude
 */
public class Treasure extends Thing implements Lootable {
    // Worth of this treasure.
    private double value;

    /**
     * Make a treasure.
     * Note: Character replacement rules from Thing apply
     * @param shortDescription short name for this item. (This will also be
     *                         used as the long description)
     * @param value worth of this item
     */
    public Treasure(String shortDescription, double value) {
        super(shortDescription, shortDescription);
        this.value = value;
    }

    /**
     * Gets the worth of the item.
     * @return the worth of this item
     */
    @Override
    public double getValue() {
        return value;
    }

    /**
     * Can the looter loot me.
     * @return true if looter is an instance of Player; else false
     * @inheritDoc
     */
    @Override
    public boolean canLoot(Thing looter) {
        return looter instanceof Player;
    }

    /**
     * Get encoded representation. Value is rounded to 5 decimal places.
     * @return $;V;S where V=value, S=raw short description
     * (eg "$;14.50000;box")
     */
    public String repr() {
        return "$;"+String.format("%.5f;", this.value)+this.getShort();
    }

    /**
     * Factory to create Treasure from a String
     * @param encoded repr() form of the object
     * @return decoded Object or null for failure. Failures include:
     * null parameters, empty input or improperly encoded input.
     */
    public static Treasure decode(String encoded) {
        try {
            String[] parts = encoded.split(";");
            if (!parts[0].equals("$")) {
                return null;
            }
            double value = Double.parseDouble(parts[1]);
            String shortDesc = parts[2];
            Treasure decoded = new Treasure(shortDesc, value);
            return decoded;
        } catch (ArrayIndexOutOfBoundsException|IllegalArgumentException e) {
            return null;
        }
    }
}
