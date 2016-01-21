package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import com.google.common.eventbus.Subscribe;
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
}
