import java.util.*;

/** Class Explorer: A player who doesn't modify the map.
 *  @author Howie L.
 */
public class Explorer extends Thing implements Mob {
    private int health;
    private List<Thing> things = new ArrayList<>();

    /** A constructor of class Explorer: Copy details from another Player
     * @param p The player to copy from
     */
    public Explorer(Explorer p) {
        super(p.getShort(), p.getLong());
        this.health = p.getHealth();
        this.things = p.getContents();
    }

    /** A constructor of class Explorer: Starting health is set to the maximum
     * 10. See Thing constructor for details of shortDesc and longDesc.
     */
    public Explorer(String shortDesc,
                    String longDesc) {
        super(shortDesc, longDesc);
        this.health = 10;
    }

    /** A constructor of class Explorer: See Thing constructor for details of
     * shortDesc and longDesc.
     * @param shortDesc
     * @param longDesc
     * @param health Starting health
     */
    public Explorer(String shortDesc,
                    String longDesc,
                    int health) {
        super(shortDesc, longDesc);
        if (health > 10) this.health = 10;
        else if (health < 0) this.health = 0;
        else this.health = health;
    }

    /** Get long description of the Thing (possibly with extra info at subclass'
     * discretion)
     * @return Long description followed immediately by either: " with ??
     * health" where ?? is the current health OR "(fainted)" if the character
     * is not alive.
     */
    @Override
    public String getDescription() {
        Integer health = this.health;
        if (health == 0) return getLong()+"(fainted)";
        return getLong()+" with "+health.toString()+" health";
    }

    /** Attempt to damage this Mob. Health is bounded below by zero.
     * @param d Amount of damage
     */
    public void takeDamage(int d) {
        if (this.health > d) this.health -= d;
        else this.health = 0;
    }

    /** Amount of damage this mob does in one hit
     * @return Constant integer 1
     */
    public int getDamage() {
        return 1;
    }

    /** Keep fighting as long as both this and mob are alive. mob takes our
     * damage then we take their damage
     * @param mob The target to fight with
     */
    public void fight(Mob mob) {
        while (this.isAlive() && mob.isAlive()) {
            mob.takeDamage(this.getDamage());
            this.takeDamage(mob.getDamage());
        }
    }

    /** Whether this Mob wants to fight mob
     * @param mob A possible target to fight
     * @return false
     */
    public boolean wantsToFight(Mob mob) {
        return false;
    }

    /** Get the current mob health
     * @return current health
     */
    public int getHealth() {
        return this.health;
    }

    /** Whether the current mob health is above 0
     * @return true if the Mob's health > 0
     */
    public boolean isAlive() {
        if (this.health>0) return true;
        return false;
    }

    /** If true, set health to starting health.
     * @param b life status Note: setting a Mob back to alive will set its
     * health back to its starting/max health (even if it already alive).
     * Setting a Mob with >0 health to false will set its health to 0
     */
    public void setAlive(boolean b) {
        if (b==true) this.health = 10;
        else this.health=0;
    }

    /** Put a Thing object into player's inventory
     * @param t Thing to add to inventory
     */
    public void add(Thing t) {
        this.things.add(t);
    }

    /** Get the contents of the player's inventory
     * @return A list of Things in the inventory
     */
    public List<Thing> getContents() {
        return this.things;
    }

    /** Remove a Thing from inventory. Fails silently if item isn't present
     * @param t Thing to remove
     */
    public void drop(Thing t) {
        this.things.remove(t);
    }

    /** Remove a Thing from inventory.
     * @param s Short description of the thing to remove
     * @return The Thing removed or null if not found
     */
    public Thing drop(String s) {
        for (int i = 0; i<this.things.size(); ++i) {
            Thing t = things.get(i);
            if (t.getShort() == s) {
                things.remove(t);
                return t;
            }
        }
        return null;
    }
}
