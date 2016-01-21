package mod.steamnsteel.mcgui.client.gui;

import java.util.HashSet;

public class MouseCapture {
    private static final HashSet<ControlBase> capturingControls = new HashSet<ControlBase>();
    public static void register(ControlBase control) {
        capturingControls.add(control);
    }

    public static void unregister(ControlBase control) {
        capturingControls.remove(control);
    }

    public static Iterable<ControlBase> getCapturedControls() {
        return capturingControls;
    }

    public static boolean isCapturing(ControlBase control) {
        return capturingControls.contains(control);
    }
}
