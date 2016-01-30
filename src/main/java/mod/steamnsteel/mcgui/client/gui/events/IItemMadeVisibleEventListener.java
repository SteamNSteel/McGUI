package mod.steamnsteel.mcgui.client.gui.events;

import mod.steamnsteel.mcgui.client.gui.ControlBase;
import mod.steamnsteel.mcgui.client.gui.IGuiTemplate;
import mod.steamnsteel.mcgui.client.gui.IModelView;
import mod.steamnsteel.mcgui.client.gui.controls.ScrollPaneControl;

public interface IItemMadeVisibleEventListener<TModel, TChildComponent extends ControlBase & IGuiTemplate<TChildComponent> & IModelView<TModel>>
{
     void onItemMadeVisible(ScrollPaneControl scrollPaneControl, TChildComponent childComponent, TModel model);
}
