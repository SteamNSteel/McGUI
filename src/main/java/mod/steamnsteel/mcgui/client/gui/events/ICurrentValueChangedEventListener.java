package mod.steamnsteel.mcgui.client.gui.events;

import mod.steamnsteel.mcgui.client.gui.ControlBase;

public interface ICurrentValueChangedEventListener<TValue>
{
    void onCurrentValueChanged(ControlBase control, TValue previousValue, TValue newValue);
}
