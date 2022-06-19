/** Interface Lootable: Anything which can be picked up/carried by a player
 * @author Howie L.
 */
public interface Lootable {
    /** Returns the value of the item.
     */
    double getValue();

    /** Whether the looter can pick up this object.
     */
    boolean canLoot(Thing looter);
}
