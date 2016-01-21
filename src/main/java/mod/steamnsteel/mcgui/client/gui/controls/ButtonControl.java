package mod.steamnsteel.mcgui.client.gui.controls;

import mod.steamnsteel.mcgui.client.gui.ControlBase;
import mod.steamnsteel.mcgui.client.gui.GuiLogger;
import mod.steamnsteel.mcgui.client.gui.GuiRenderer;
import mod.steamnsteel.mcgui.client.gui.GuiTexture;
import mod.steamnsteel.mcgui.client.gui.events.IButtonPressedEventListener;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ButtonControl extends ControlBase
{
    private GuiTexture defaultTexture;
    private GuiTexture pressedTexture;
    private GuiTexture hoverTexture;
    private GuiTexture disabledTexture;
    private GuiTexture currentTexture;

    public ButtonControl(GuiRenderer guiRenderer)
    {
        super(guiRenderer);
    }

    public ButtonControl(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        super(guiRenderer, componentBounds);
    }

    public ButtonControl(GuiRenderer guiRenderer, int width, int height)
    {
        super(guiRenderer, width, height);
    }

    @Override
    public void draw()
    {
        super.draw();
        if (currentTexture != null)
        {
            getGuiRenderer().drawComponentTexture(this, currentTexture);
        }
    }

    @Override
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton)
    {
        if (mouseButton == 0)
        {
            onButtonPressedInternal();
            currentTexture = defaultTexture;
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseClick(ReadablePoint point, int mouseButton)
    {
        if (mouseButton == 0)
        {
            currentTexture = pressedTexture;
            return true;
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Button Pressed Event Handling
    /////////////////////////////////////////////////////////////////////////////

    private void onButtonPressedInternal() {
        onButtonPressed();

        fireButtonPressedEvent();
    }

    protected void onButtonPressed() {
    }

    private void fireButtonPressedEvent()
    {
        for (final IButtonPressedEventListener currentValueChangedEventListener : buttonPressedEventListeners)
        {
            try {
                currentValueChangedEventListener.onButtonPressed(this);
            } catch (RuntimeException e) {
                GuiLogger.warning("Exception in an ICurrentValueChangedEventListener %s", e);
            }
        }
    }

    private List<IButtonPressedEventListener> buttonPressedEventListeners = new ArrayList<IButtonPressedEventListener>();

    @SuppressWarnings("unused")
    public void addOnButtonPressedEventListener(IButtonPressedEventListener listener) {
        buttonPressedEventListeners.add(listener);
    }
    @SuppressWarnings("unused")
    public void removeOnButtonPressedEventListener(IButtonPressedEventListener listener) {
        buttonPressedEventListeners.remove(listener);
    }


    public GuiTexture getDefaultTexture()
    {
        return defaultTexture;
    }

    public void setDefaultTexture(GuiTexture defaultTexture)
    {
        if (currentTexture == this.defaultTexture) {
            currentTexture = defaultTexture;
        }
        this.defaultTexture = defaultTexture;
    }

    public GuiTexture getPressedTexture()
    {
        return pressedTexture;
    }

    public void setPressedTexture(GuiTexture pressedTexture)
    {
        this.pressedTexture = pressedTexture;
    }

    public GuiTexture getHoverTexture()
    {
        return hoverTexture;
    }

    public void setHoverTexture(GuiTexture hoverTexture)
    {
        this.hoverTexture = hoverTexture;
    }

    public GuiTexture getDisabledTexture()
    {
        return disabledTexture;
    }

    public void setDisabledTexture(GuiTexture disabledTexture)
    {
        this.disabledTexture = disabledTexture;
    }
}
