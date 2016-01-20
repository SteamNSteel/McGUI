package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

/**
 * Created by codew on 17/01/2016.
 */
public class ClientNetworkProxy extends CommonNetworkProxy
{
    @Override
    public void init() {
        super.init();
        getNetwork().registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.CLIENT);
        final KeyBinding exampleMod = new KeyBinding("Start Project Table", Keyboard.KEY_P, "ExampleMod");
        ClientRegistry.registerKeyBinding(exampleMod);
    }



}
