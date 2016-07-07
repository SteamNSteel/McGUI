package mod.steamnsteel.mcgui.client.gui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import java.io.IOException;

public abstract class McGUIScreen extends GuiScreen
{
    private final McGUIMixin mixin;
    private ControlBase rootControl = null;

    protected McGUIScreen()
    {
        mixin = new McGUIMixin();
    }

    public final void setRootControl(ControlBase rootControl) {
        this.rootControl = rootControl;
        mixin.rootControl = rootControl;
    }

    protected final void addChild(ControlBase childControl) {
        rootControl.addChild(childControl);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        if (rootControl == null) return;

        final int eventX = Mouse.getEventX() * width / mc.displayWidth;
        final int eventY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        final int eventButton = Mouse.getEventButton();
        final boolean isTouchscreen = mc.gameSettings.touchscreen;

        mixin.handleMouseInput(eventX, eventY, eventButton, isTouchscreen);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (rootControl == null) return;
        rootControl.draw();
    }
}
