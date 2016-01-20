package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by codew on 17/01/2016.
 */
public class CommonNetworkProxy
{
    private SimpleNetworkWrapper network;

    public SimpleNetworkWrapper getNetwork()
    {
        return network;
    }

    public void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(ExampleMod.MODID);
        network.registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.SERVER);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        // DEBUG
        System.out.println("Key Input Event");

        // make local copy of key binding array
        KeyBinding[] keyBindings = ClientProxy.keyBindings;

        // check each enumerated key binding type for pressed and take appropriate action
        if (keyBindings[0].isPressed())
        {
            // DEBUG
            System.out.println("Key binding ="+keyBindings[0].getKeyDescription());

            // do stuff for this key binding here
            // remember you may need to send packet to server
        }
        if (keyBindings[1].isPressed())
        {
            // DEBUG
            System.out.println("Key binding ="+keyBindings[1].getKeyDescription());

            // do stuff for this key binding here
            // remember you may need to send packet to server
        }
    }
}
