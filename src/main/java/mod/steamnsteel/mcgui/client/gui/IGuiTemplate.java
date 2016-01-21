package mod.steamnsteel.mcgui.client.gui;

public interface IGuiTemplate<TControl extends ControlBase>
{
    TControl construct();

}
