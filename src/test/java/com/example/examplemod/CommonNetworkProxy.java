package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
