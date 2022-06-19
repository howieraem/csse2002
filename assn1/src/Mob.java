/** Interface Mob: Anything which can fight
 * @author Howie L.
 */
public interface Mob {
    /** Fight another mob. This mob gets first hit. Each mob takes turns until
     * one of them falls over.
     */
    void fight(Mob mob);

    /** Whether this Mob wants to fight mob.
     */
    boolean wantsToFight(Mob mob);

    /** Whether this Mob is alive.
     */
    boolean isAlive();

    /** Set this Mob's health status.
     */
    void setAlive(boolean b);

    /** The amount of damage this mob does in one hit.
     */
    int getDamage();

    /** Attempt to damage this Mob. Health is bounded below by zero.
     */
    void takeDamage(int d);
}
