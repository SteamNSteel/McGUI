package mod.steamnsteel.client.gui;

public final class GuiLogger
{
    private static IDebugLogger logger = null;

    public static void warning(String formattedString, Object... parameters)
    {
        if (logger != null) {
            logger.warning(formattedString, parameters);
        }
    }

    public static void info(String formattedString, Object... parameters)
    {
        if (logger != null) {
            logger.info(formattedString, parameters);
        }
    }


    public static void setLogger(IDebugLogger logger) {

        GuiLogger.logger = logger;
    }
}
