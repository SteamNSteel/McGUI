package mod.steamnsteel.mcgui.client.gui.controls;

import mod.steamnsteel.mcgui.client.gui.ControlBase;
import mod.steamnsteel.mcgui.client.gui.GuiRenderer;
import mod.steamnsteel.mcgui.client.gui.GuiTexture;
import org.lwjgl.util.Rectangle;

public class TexturedPaneControl extends ControlBase
{
    private final GuiTexture texture;

    public TexturedPaneControl(GuiRenderer guiRenderer, Rectangle componentBounds, GuiTexture texture)
    {
        super(guiRenderer, componentBounds);
        this.texture = texture;
    }

    public TexturedPaneControl(GuiRenderer guiRenderer, int width, int height, GuiTexture texture)
    {
        super(guiRenderer, width, height);
        this.texture = texture;
    }

    @Override
    public void draw()
    {
        getGuiRenderer().drawComponentTexture(this, texture);
        super.draw();
    }
}
