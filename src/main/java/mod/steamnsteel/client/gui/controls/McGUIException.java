package mod.steamnsteel.client.gui.controls;

/**
 * Created by codew on 18/01/2016.
 */
@SuppressWarnings("unused")
public class McGUIException extends RuntimeException
{
    private final boolean hasFries;
    private final boolean hasDrink;

    public boolean hasFries() { return hasFries; }
    public boolean hasDrink() { return hasDrink; }

    public McGUIException(String message) {
        super(message);
        hasFries = false;
        hasDrink = false;
    }
    public McGUIException(String message, boolean hasFries, boolean hasDrink) {
        super(message);
        this.hasFries = hasFries;
        this.hasDrink = hasDrink;}
}
