import org.junit.Test;
import static org.junit.Assert.*;

/** Class TreasureTest: JUnit4 Testing Class for Class Treasure
 * @author Howie L.
 */
public class TreasureTest {
    /** Method treasureValueTest(): Test constructor and getValue() with
     * negative number, zero and positive number
     */
    @Test
    public void treasureValueTest() {
        Treasure tr1 = new Treasure("tr1", 2.41);
        Treasure tr2 = new Treasure("tr2", 0);
        Treasure tr3 = new Treasure("tr3", -2);
        assertEquals(2.41, tr1.getValue(), 0.000001);
        assertEquals(0, tr2.getValue(), 0.000001);
        assertEquals(-2, tr3.getValue(), 0.000001);
    }

    /** Method treasureLootTest1(): Test canLoot() with an Explorer
     */
    @Test
    public void treasureLootTest1() {
        Treasure tr1 = new Treasure("tr1", 2.41);
        Explorer looter1 = new Explorer("", "ld2", -1);
        assertTrue(tr1.canLoot(looter1));
    }

    /** Method treasureLootTest2(): Test canLoot() with another Explorer
     */
    @Test
    public void treasureLootTest2() {
        Treasure tr1 = new Treasure("tr1", 2.41);
        Explorer looter2 = new Explorer(null, null);
        assertTrue(tr1.canLoot(looter2));
    }

    /** Method treasureLootTest3(): Test canLoot() with a Thing
     */
    @Test
    public void treasureLootTest3() {
        Treasure tr1 = new Treasure("tr1", 2.41);
        Thing looter1 = new Thing(null, "ld2");
        assertFalse(tr1.canLoot(looter1));
    }
}
