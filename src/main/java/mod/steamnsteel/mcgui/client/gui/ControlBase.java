package mod.steamnsteel.mcgui.client.gui;

import org.lwjgl.util.*;

import java.util.ArrayList;
import java.util.List;

public class ControlBase
{
    private final Rectangle componentBounds = new Rectangle();
    private final GuiRenderer guiRenderer;
    private ControlBase parent = null;
    private final List<ControlBase> children = new ArrayList<ControlBase>(10);

    public ControlBase(GuiRenderer guiRenderer) {
        this(guiRenderer, new Rectangle());
    }

    public ControlBase(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        this.guiRenderer = guiRenderer;
        this.componentBounds.setBounds(componentBounds);
        onResizeInternal();
    }
    public ControlBase(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }


    public void setLocation(int x, int y)
    {
        componentBounds.setLocation(x, y);
        onResizeInternal();
    }
    public void setLocation(ReadablePoint point) {
        componentBounds.setLocation(point);
        onResizeInternal();
    }
    public void setSize(int width, int height) {
        componentBounds.setSize(width, height);
        onResizeInternal();
    }
    public void setSize(ReadableDimension dimensions) {
        componentBounds.setSize(dimensions);
        onResizeInternal();
    }

    public void draw() {
        for (final ControlBase child : children)
        {
            child.draw();
        }
    }

    public ReadableRectangle getBounds() {
        return componentBounds;
    }

    public void addChild(ControlBase child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(ControlBase child) {
        children.remove(child);
        child.setParent(null);
    }

    public ControlBase getParent() {
        return parent;
    }

    public void setParent(ControlBase parent)
    {
        this.parent = parent;
    }

    public boolean mouseClicked(ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseClicked(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(final ReadablePoint point) {
                return onMouseClickInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseReleased(final ReadablePoint point, final int mouseButton)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseReleased(localPoint, mouseButton);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseReleasedInternal(point, mouseButton);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseMoved(final ReadablePoint point) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseMoved(localPoint);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseMovedInternal(point);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragged(final ReadablePoint point, final ReadablePoint delta, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseDragged(localPoint, delta, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDraggedInternal(point, delta, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragStarted(final ReadablePoint point, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseDragStarted(localPoint, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDragStartedInternal(point, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseDragEnded(final ReadablePoint point, final int buttons) {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseDragEnded(localPoint, buttons);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return onMouseDragEndedInternal(point, buttons);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseWheelUp(final ReadablePoint point, final int scrollAmount)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseWheelUp(localPoint, scrollAmount);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return mouseWheelUpInternal(point, scrollAmount);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    public boolean mouseWheelDown(final ReadablePoint point, final int scrollAmount)
    {
        IMouseCallback mouseCallback = new IMouseCallback() {
            @Override
            public boolean checkChild(ControlBase child, ReadablePoint localPoint) {
                return child.mouseWheelDown(localPoint, scrollAmount);
            }

            @Override
            public boolean checkCurrent(ReadablePoint point) {
                return mouseWheelDownInternal(point, scrollAmount);
            }
        };
        return checkMouseBoundsAndPropagate(point, mouseCallback);
    }

    private boolean checkMouseBoundsAndPropagate(final ReadablePoint point, final IMouseCallback callback) {
        final Rectangle realControlBounds = new Rectangle();

        Point localPoint = new Point();
        //Logger.info("event triggered in %s @ %s - %s", this.getClass().getSimpleName(), this.getBounds(), point);

        boolean handled = false;
        for (final ControlBase child : children)
        {
            realControlBounds.setSize(child.getBounds());

            localPoint.setLocation(point);
            localPoint.untranslate(child.getBounds());
            if (realControlBounds.contains(localPoint)) {
                if (callback.checkChild(child, localPoint)) {
                    handled = true;
                    break;
                }
            }
        }

        if (!handled) {
            handled = callback.checkCurrent(point);
        }
        return handled;
    }

    public GuiRenderer getGuiRenderer()
    {
        return guiRenderer;
    }

    private interface IMouseCallback {
        boolean checkChild(ControlBase child, ReadablePoint localPoint);

        boolean checkCurrent(ReadablePoint point);
    }

    protected void captureMouse() {
        MouseCapture.register(this);
    }

    protected void releaseMouse() {
        MouseCapture.unregister(this);
    }

    /////////////////////////////////////////////////////////////////////////////
    // Internal event handling
    /////////////////////////////////////////////////////////////////////////////
    private boolean onMouseClickInternal(ReadablePoint point, int mouseButton)
    {
        return onMouseClick(point, mouseButton);
    }

    private boolean onMouseReleasedInternal(ReadablePoint point, int mouseButton) {
        return onMouseRelease(point, mouseButton);
    }

    private boolean onMouseMovedInternal(ReadablePoint point) {
        return onMouseMoved(point);
    }

    private boolean onMouseDraggedInternal(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        return onMouseDragged(point, delta, mouseButton);
    }

    private boolean onMouseDragStartedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragStarted(point, mouseButton);
    }

    private boolean onMouseDragEndedInternal(ReadablePoint point, int mouseButton) {
        return onMouseDragEnded(point, mouseButton);
    }

    private boolean mouseWheelUpInternal(ReadablePoint point, int scrollAmount)
    {
        return onMouseWheelUp(point, scrollAmount);
    }

    private boolean mouseWheelDownInternal(ReadablePoint point, int scrollAmount)
    {
        return onMouseWheelDown(point, scrollAmount);
    }

    private void onResizeInternal() {
        onResized(componentBounds);
    }



    /////////////////////////////////////////////////////////////////////////////
    // Events for subclasses
    /////////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseClick(ReadablePoint point, int mouseButton)
    {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseRelease(ReadablePoint point, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseMoved(ReadablePoint point) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragged(ReadablePoint point, ReadablePoint delta, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragStarted(ReadablePoint point, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseDragEnded(ReadablePoint point, int mouseButton) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseWheelUp(ReadablePoint point, int scrollAmount) {
        return false;
    }
    @SuppressWarnings("UnusedParameters")
    protected boolean onMouseWheelDown(ReadablePoint point, int scrollAmount) {
        return false;
    }

    @SuppressWarnings("UnusedParameters")
    protected void onResized(ReadableRectangle componentBounds) { }

}
