package mod.steamnsteel.mcgui.client.gui.controls;

import mod.steamnsteel.mcgui.client.gui.*;
import mod.steamnsteel.mcgui.client.gui.events.ICurrentValueChangedEventListener;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.ReadableRectangle;
import java.util.ArrayList;
import java.util.List;

public class ScrollbarControl extends ControlBase
{
    private final GuiTexture activeHandle;
    private final GuiTexture inactiveHandle;

    private int minimumValue = 0;
    private int maximumValue = 100;
    private int currentValue = 0;
    private int mouseOffset = 0;

    private GuiTexture currentTexture;
    private boolean enabled;

    public ScrollbarControl(GuiRenderer guiRenderer, GuiTexture activeHandle, GuiTexture inactiveHandle)
    {
        super(guiRenderer);
        this.activeHandle = activeHandle;
        this.inactiveHandle = inactiveHandle;
        currentTexture = inactiveHandle;

        onResized(getBounds());
    }

    @Override
    public void draw()
    {
        if (!enabled) {
            return;
        }
        getGuiRenderer().drawComponentTextureWithOffset(this, currentTexture, 0, getHandleTop());
    }

    public int getMinimumValue()
    {
        return minimumValue;
    }

    public void setMinimumValue(int minimumValue)
    {
        if (minimumValue < maximumValue) {
            throw new McGUIException("Attempt to set a scrollbar's maximum to less than it's minimum");
        }

        this.minimumValue = minimumValue;
        if (currentValue < minimumValue) {
            currentValue = minimumValue;
        }
    }

    private int usableScrollHeight = 0;

    @Override
    protected void onResized(ReadableRectangle componentBounds) {
        if (inactiveHandle != null)
        {
            usableScrollHeight = componentBounds.getHeight() - inactiveHandle.getBounds().getHeight();
        }
    }

    @Override
    protected boolean onMouseClick(ReadablePoint point, int mouseButton) {
        if (!enabled) {
            return false;
        }
        if (mouseButton == 0) {
            currentTexture = activeHandle;
            mouseOffset = point.getY() - getHandleTop();
            captureMouse();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton) {
        if (!enabled) {
            return false;
        }
        if (mouseButton == 0) {
            currentTexture = inactiveHandle;
            releaseMouse();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onMouseDragged(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        if (!enabled) {
            return false;
        }
        int newY = point.getY() - mouseOffset;
        if (newY < 0) newY = 0;
        if (newY > usableScrollHeight) newY = usableScrollHeight;

        double percentage= newY / (double)usableScrollHeight;

        int discreteValues = maximumValue - minimumValue;

        setCurrentValue(minimumValue + (int)(percentage * discreteValues));
        
        return true;
    }

    @Override
    public boolean mouseWheelDown(ReadablePoint point, int scrollAmount)
    {
        setCurrentValue(getCurrentValue() + 5);
        return true;
    }

    @Override
    public boolean mouseWheelUp(ReadablePoint point, int scrollAmount)
    {
        setCurrentValue(getCurrentValue() + -5);
        return true;
    }

    public int getMaximumValue()
    {
        return maximumValue;
    }

    public void setMaximumValue(int maximumValue)
    {
        if (maximumValue < minimumValue) {
            throw new McGUIException("Attempt to set a scrollbar's maximum to less than it's minimum");
        }

        this.maximumValue = maximumValue;
        if (currentValue > maximumValue) {
            currentValue = maximumValue;
        }
    }

    public int getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(int newValue)
    {
        int previousValue = currentValue;

        if (newValue < minimumValue){
            newValue = minimumValue;
        }
        if (newValue > maximumValue) {
            newValue = maximumValue;
        }


        if (previousValue != newValue) {
            currentValue = newValue;
            fireOnCurrentValueChangedEvent(previousValue, newValue);
        }

        //Logger.info("Scrollbar current value changed to %d", newValue);
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    /////////////////////////////////////////////////////////////////////////////
    // Current Value Changed Event Handling
    /////////////////////////////////////////////////////////////////////////////

    private void fireOnCurrentValueChangedEvent(int previousValue, int newValue)
    {
        for (final ICurrentValueChangedEventListener currentValueChangedEventListener : currentValueChangedEventListeners)
        {
            try {
                currentValueChangedEventListener.onCurrentValueChanged(this, previousValue, newValue);
            } catch (RuntimeException e) {
                GuiLogger.warning("Exception in an ICurrentValueChangedEventListener %s", e);
            }
        }
    }

    private final List<ICurrentValueChangedEventListener> currentValueChangedEventListeners = new ArrayList<ICurrentValueChangedEventListener>(1);

    public void addOnCurrentValueChangedEventListener(ICurrentValueChangedEventListener listener) {
        currentValueChangedEventListeners.add(listener);
    }
    public void removeOnCurrentValueChangedEventListener(ICurrentValueChangedEventListener listener) {
        currentValueChangedEventListeners.remove(listener);
    }

    private int getHandleTop() {
        final double percentageLocation = (currentValue - minimumValue) / (double)(maximumValue - minimumValue);
        return (int) (percentageLocation * usableScrollHeight);
    }
}
