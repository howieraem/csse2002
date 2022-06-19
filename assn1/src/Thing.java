/** Class Thing: Base class for anything which can be found in a Room.
 * @author Howie L.
 */
public class Thing implements java.io.Serializable {
    private String shortDesc;
    private String longDesc;
    private String rawShortDesc;
    private String rawLongDesc;

    /** Constructor of the class Thing. Note: \n, \r and semi-colons
     * in the parameters will be replaced by *
     * @param shortDesc A short name or description for the Thing
     * @param longDesc A more detailed description for the Thing
     */
    public Thing(String shortDesc,
                 String longDesc) {
        this.shortDesc = shortDesc;
        setShort(shortDesc);
        this.longDesc = longDesc;
        setLong(longDesc);
    }

    /** Allows subclasses to read the raw short description value.
     * ("Raw" here refers to the description passed to the Thing constructor
     * _after_ special characters have been replaced).
     * @return The raw shortDesc value
     */
    protected String getShort() {
        return rawShortDesc;
    }

    /** Allows subclasses to read the raw long description value.
     * ("Raw" here refers to the description passed to the Thing constructor
     * _after_ special characters have been replaced).
     * @return The raw longDesc value
     */
    protected String getLong() {
        return rawLongDesc;
    }

    /** Change the short description for the Thing.
     * @param s A short name or description for the Thing.
     * Note: Newlines and semi-colons in the parameter will be replaced by *
     */
    protected void setShort(String s) {
        String desc = s.replace("\n","*");
        desc = desc.replace("\r","*");
        desc = desc.replace(";","*");
        this.rawShortDesc = desc;
    }

    /** Change the long description for the Thing.
     * @param s A detailed description for the Thing.
     * Note: Newlines and semi-colons in the parameter will be replaced by *
     */
    protected void setLong(String s) {
        String desc = s.replace("\n","*");
        desc = desc.replace("\r","*");
        desc = desc.replace(";","*");
        this.rawLongDesc = desc;
    }

    /** Get long description of the Thing (possibly with extra info at subclass'
     * discretion).
     * @return Long description. Note: not to be used for saving or encoding
     * due to possible extra info being included.
     */
    public String getDescription() {
        return longDesc;
    }

    /** Get short description of the Thing
     * @return Short description. Note: This name is used to represent
     * the Thing in text and to choose it in dialogs.
     */
    public String getShortDescription() {
        return shortDesc;
    }
}
