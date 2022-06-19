/** Class Critter: A non-player Lootable Mob.
 * @author Howie L.
 */
public class Critter extends Thing implements Lootable, Mob {
    private double value;
    private int health;

    /** Constructor of the class Critter
     * @param shortDesc Name or short description for this Mob
     * @param longDesc Longer description for this Mob
     * @param value Worth of Mob when looted
     * @param health Starting health of Mob. If negative, use zero instead.
     */
    public Critter(String shortDesc,
                   String longDesc,
                   double value,
                   int health) {
        super(shortDesc, longDesc);
        this.value = value;
        if (health < 0) this.health = 0;
        else this.health = health;
    }

    /** Get the long desctription of Mob showing whether it is dead
     * @return Long description, immediately followed by "(fainted)"
     * iff the critter is not alive
     */
    @Override
    public String getDescription() {
        if (this.health <= 0) return getLong()+"(fainted)";
        return getLong();
    }

    /** Attempt to damage this Mob. Health is bounded below by zero.
     * @param d Amount of damage
     */
    public void takeDamage(int d) {
        if (this.health > d) this.health -= d;
        else this.health = 0;
    }

    /** Amount of damage this mob does in one hit
     * @return Constant integer 2
     */
    public int getDamage() {
        return 2;
    }

    /** Get the item's value
     * @return The value
     */
    public double getValue() {
        return this.value;
    }

    /** Whether the looter can pick up this object
     * @param looter Object try to collect
     * @return true iff looter is an explorer and your health is zero
     */
    public boolean canLoot(Thing looter) {
        if ((looter instanceof Explorer) && this.health == 0) return true;
        return false;
    }

    /** Fight another mob. This mob gets first hit. Each mob takes turns until
     * one of them falls over.
     * @param mob The target to fight with
     */
    public void fight(Mob mob) {
        while (this.isAlive() && mob.isAlive()) {
            this.takeDamage(mob.getDamage());
            mob.takeDamage(this.getDamage());
        }
    }

    /** Whether this Mob wants to fight mob
     * @param mob A possible target to fight
     * @return true if mob is an Explorer
     */
    public boolean wantsToFight(Mob mob) {
        if (mob instanceof Explorer) return true;
        return false;
    }

    /** Whether the current mob health is above 0
     * @return true if the Mob's health > 0
     */
    public boolean isAlive() {
        if (this.health > 0) return true;
        return false;
    }

    /** If true, set health to starting health.
     * @param b life status Note: setting a Mob back to alive will set its
     * health back to its starting/max health (even if it already alive).
     * Setting a Mob with >0 health to false will set its health to 0
     */
    public void setAlive(boolean b) {
        if (b == true) this.health = 10;
        else this.health = 0;
    }

    /** Get the current mob health
     * @return current health
     */
    public int getHealth() {
        return this.health;
    }
}
