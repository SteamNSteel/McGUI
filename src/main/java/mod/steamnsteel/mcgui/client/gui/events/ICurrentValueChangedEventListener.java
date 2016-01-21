package mod.steamnsteel.mcgui.client.gui.events;

import mod.steamnsteel.mcgui.client.gui.ControlBase;

public interface ICurrentValueChangedEventListener
{
    void invoke(ControlBase scrollbarControl, int previousValue, int newValue);
}
