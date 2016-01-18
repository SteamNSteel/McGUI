package mod.steamnsteel.mcgui.client.gui.events;

import mod.steamnsteel.mcgui.client.gui.Control;

public interface ICurrentValueChangedEventListener
{
    void invoke(Control scrollbarControl, int previousValue, int newValue);
}
