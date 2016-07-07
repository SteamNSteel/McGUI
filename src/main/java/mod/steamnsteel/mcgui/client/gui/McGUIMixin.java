package mod.steamnsteel.mcgui.client.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;

/**
 * Created by codew on 3/07/2016.
 */
public class McGUIMixin
{
    /////////////////////////////////////////////////////////////////////////////
    // Control Event handling
    /////////////////////////////////////////////////////////////////////////////
    private final Point lastMouseLocation = new Point();
    private final Point currentMouseLocation = new Point();
    private boolean isDragging;
    private int dragButton;
    private final Rectangle bounds = new Rectangle();
    private int eventButton;
    private int touchValue;
    private long lastMouseEvent;

    ControlBase rootControl;


    public void handleMouseInput(int eventX, int eventY, int eventButton, boolean isTouchscreen) {


        bounds.setBounds(rootControl.getBounds());
        if (!bounds.contains(eventX, eventY)) {
            return;
        }
        eventX -= bounds.getX();
        eventY -= bounds.getY();

        currentMouseLocation.setLocation(eventX, eventY);

        if (Mouse.getEventButtonState())
        {
            if (isTouchscreen && touchValue++ > 0)
            {
                return;
            }

            this.eventButton = eventButton;
            lastMouseEvent = Minecraft.getSystemTime();
            rootControl.mouseClicked(currentMouseLocation, this.eventButton);
        }
        else if (this.eventButton != -1)
        {
            if (isTouchscreen && --touchValue > 0)
            {
                return;
            }

            this.eventButton = -1;
            rootControl.mouseReleased(currentMouseLocation, this.eventButton);
            for (final ControlBase control : MouseCapture.getCapturedControls())
            {
                control.mouseReleased(currentMouseLocation, this.eventButton);
            }
            if (isDragging && this.eventButton == dragButton) {
                //Logger.info("Mouse Drag Ended %s", currentMouseLocation);
                rootControl.mouseDragEnded(currentMouseLocation, this.eventButton);
                for (final ControlBase control : MouseCapture.getCapturedControls())
                {
                    control.mouseDragEnded(currentMouseLocation, this.eventButton);
                }
                isDragging = false;
            }
        }

        if (!currentMouseLocation.equals(lastMouseLocation)) {

            //Logger.info("Mouse Moved %s", currentMouseLocation);
            rootControl.mouseMoved(currentMouseLocation);

            if (this.eventButton != -1 && lastMouseEvent > 0L) {
                if (!isDragging) {
                    //Logger.info("Mouse Drag started %s", currentMouseLocation);
                    rootControl.mouseDragStarted(currentMouseLocation, this.eventButton);
                    isDragging = true;
                    dragButton = this.eventButton;
                } else {
                    //Logger.info("Mouse Dragged %s", currentMouseLocation);
                    Point delta = new Point(currentMouseLocation);
                    delta.untranslate(lastMouseLocation);

                    rootControl.mouseDragged(currentMouseLocation, delta, this.eventButton);
                    Point p = new Point();
                    for (final ControlBase control : MouseCapture.getCapturedControls())
                    {
                        final ReadablePoint controlLocation = GuiRenderer.getControlLocation(control);
                        p.setLocation(currentMouseLocation);
                        p.untranslate(controlLocation);
                        p.translate(rootControl.getBounds());
                        control.mouseDragged(p, delta, this.eventButton);
                    }
                }
            }
            lastMouseLocation.setLocation(currentMouseLocation);
        }

        final int scrollAmount = Mouse.getDWheel();
        if (scrollAmount != 0) {
            if (scrollAmount > 0) {
                rootControl.mouseWheelUp(currentMouseLocation, scrollAmount);
            } else {
                rootControl.mouseWheelDown(currentMouseLocation, scrollAmount);
            }
        }
    }
}
