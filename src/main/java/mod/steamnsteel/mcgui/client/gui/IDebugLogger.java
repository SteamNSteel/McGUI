package mod.steamnsteel.mcgui.client.gui;

/**
 * Created by codew on 18/01/2016.
 */
public interface IDebugLogger
{
    void warning(String formattedString, Object... parameters);

    void info(String formattedString, Object... parameters);
}
