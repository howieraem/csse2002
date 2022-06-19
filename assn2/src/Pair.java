/**
 * Helper class to store a two ints.
 * @author Howie L.
 */
public class Pair {
    /* Field */
    public int x;
    public int y;

    /**
     * Constructor of the Pair class
     * @param x First number
     * @param y Second number
     */
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    /**
     * Determines whether two pairs are equal. E.g. for pairs p and q, does
     * p.x equal q.x and p.y equal q.y?
     * @param o Object to compare to
     */
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }

    @Override
    /**
     * Object identity function
     * @return Hash code of the object
     */
    public int hashCode() {
        return this.x*97+this.y*79;
    }
}
