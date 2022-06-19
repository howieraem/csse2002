/** Class Treasure: Lootable object which doesn't fight
 * @author Howie L.
 */
public class Treasure extends Thing implements Lootable {
    private double value;

    /** Constructor of the class Treasure
     * @param shortDesc Short name for this item. (This will also be used as the long description)
     * @param value Worth of this item
     */
    public Treasure(String shortDesc,
                    double value) {
        super(shortDesc, shortDesc);
        this.value = value;
    }

    /** Get the value of the item
     * @return Worth of this item
     */
    public double getValue() {
        return this.value;
    }

    /** Whether the looter is able to pick up this object
     * @param looter Object try to collect
     * @return true if looter is an instance of Explorer; else false
     */
    public boolean canLoot(Thing looter) {
        if (looter instanceof Explorer) return true;
        return false;
    }
}
