package mod.steamnsteel.mcgui.client.gui.controls;

import com.google.common.collect.Lists;
import mod.steamnsteel.mcgui.client.gui.ControlBase;
import mod.steamnsteel.mcgui.client.gui.GuiRenderer;
import mod.steamnsteel.mcgui.client.gui.IGuiTemplate;
import mod.steamnsteel.mcgui.client.gui.IModelView;
import mod.steamnsteel.mcgui.client.gui.events.ICurrentValueChangedEventListener;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.ReadableRectangle;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("TypeParameterNamingConvention")
public class ScrollPaneControl<TModel, TChildComponentTemplate extends ControlBase & IGuiTemplate<TChildComponentTemplate> & IModelView<TModel>> extends ControlBase
{
    private ControlBase[] itemRenderers = new ControlBase[0];
    private int lastItemsListCount = 0;
    private List<TModel> items = Lists.newArrayList();
    private TChildComponentTemplate template = null;
    private ScrollbarControl scrollbar = null;
    private final ScrollbarChangedEventListener scrollbarListener = new ScrollbarChangedEventListener();
    private int scrollbarOffset;
    private int visibleItemCount;

    public ScrollPaneControl(GuiRenderer guiRenderer, Rectangle componentBounds)
    {
        super(guiRenderer, componentBounds);
    }

    public ScrollPaneControl(GuiRenderer guiRenderer, int width, int height) {
        this(guiRenderer, new Rectangle(0, 0, width, height));
    }

    public ScrollPaneControl<TModel, TChildComponentTemplate> setItemRendererTemplate(TChildComponentTemplate guiComponentTemplate) {
        template = guiComponentTemplate;
        return this;
    }

    public ScrollPaneControl<TModel, TChildComponentTemplate> setVisibleItemCount(int visibleItems) {
        if (template == null) {
            throw new McGUIException("Can't set the visible item count, a template hasn't been defined yet");
        }
        visibleItemCount = visibleItems;
        final int actualItems = visibleItemCount + 1;
        itemRenderers = new ControlBase[actualItems];
        for (int i = 0; i < actualItems; ++i) {
            itemRenderers[i] = template.construct();
            addChild(itemRenderers[i]);
        }
        return this;
    }

    public ScrollPaneControl<TModel, TChildComponentTemplate> setScrollbar(ScrollbarControl scrollbar)
    {
        if (this.scrollbar != null) {
            this.scrollbar.removeOnCurrentValueChangedEventListener(scrollbarListener);
        }

        this.scrollbar = scrollbar;
        if (scrollbar != null) {
            this.scrollbar.addOnCurrentValueChangedEventListener(scrollbarListener);
        }
        return this;
    }

    public ScrollPaneControl<TModel, TChildComponentTemplate> setItems(List<TModel> items) {
        this.items = items == null ? new ArrayList<TModel>(0) : items;
        final int maximumValue = Math.max(0, (this.items.size() - visibleItemCount) * template.getBounds().getHeight());
        scrollbar.setMaximumValue(maximumValue);
        return this;
    }

    @Override
    public boolean mouseWheelUp(ReadablePoint point, int scrollAmount)
    {
        if (scrollbar != null) {
            return scrollbar.mouseWheelUp(point, scrollAmount);
        } else
        {
            //Handle logic for a scroll pane without a scrollbar
            return false;
        }
    }

    @Override
    public boolean mouseWheelDown(ReadablePoint point, int scrollAmount)
    {
        if (scrollbar != null) {
            return scrollbar.mouseWheelDown(point, scrollAmount);
        } else
        {
            //Handle logic for a scroll pane without a scrollbar
            return false;
        }
    }

    @Override
    public void draw()
    {
        if (itemRenderers.length == 0 || items.isEmpty()) {
            return;
        }


        final ReadableRectangle templateBounds = template.getBounds();

        final int itemHeight = templateBounds.getHeight() * items.size();
        final int viewportHeight = templateBounds.getHeight() * 5;


        double scrollbarProgress = 0;
        final int usableScrollingHeight = itemHeight - templateBounds.getHeight() * visibleItemCount;


        if (items.size() != lastItemsListCount) {
            lastItemsListCount = items.size();
            scrollbar.setEnabled(usableScrollingHeight > 0);
            final int maximumValue = Math.max(0, (items.size() - visibleItemCount) * template.getBounds().getHeight());
            scrollbar.setMaximumValue(maximumValue);
            scrollbar.setCurrentValue(0);
        }

        final Rectangle viewport = new Rectangle(
                0, 0,
                templateBounds.getWidth(), viewportHeight);

        getGuiRenderer().startViewport(this, viewport);

        if (usableScrollingHeight > 0)
        {
            scrollbarProgress = scrollbarOffset / (double) usableScrollingHeight;
        }
        final double itemProgress = Math.max(0, scrollbarProgress * (items.size() - visibleItemCount));

        final int itemIndex = (int)Math.floor(itemProgress);
        final int itemOffset = scrollbarOffset % templateBounds.getHeight();
        for (int i = 0; i < itemRenderers.length; ++i)
        {
            final ControlBase itemRenderer = itemRenderers[i];
            TModel model = null;
            if (itemIndex + i < items.size()) {
                model = items.get(itemIndex + i);
            }

            //This is unchecked, but the generic constraints ensure this cast is possible.
            if (itemRenderer instanceof IModelView)
            {
                ((IModelView<TModel>) itemRenderer).setModel(model);
            }

            itemRenderer.setLocation(0, templateBounds.getHeight() * i - itemOffset);
        }

        super.draw();

        getGuiRenderer().endViewport();
    }

    public class ScrollbarChangedEventListener implements ICurrentValueChangedEventListener
    {

        @Override
        public void invoke(ControlBase scrollbarControl, int previousValue, int newValue)
        {
            scrollbarOffset = newValue;
        }
    }
}
