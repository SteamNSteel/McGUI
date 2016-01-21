package mod.steamnsteel.mcgui.client.gui;

public interface IDebugLogger
{
    void warning(String formattedString, Object... parameters);

    void info(String formattedString, Object... parameters);
}
