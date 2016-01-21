package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

public class ClientNetworkProxy extends CommonNetworkProxy
{
    public static KeyBinding exampleModKeyBinding;

    @Override
    public void init() {
        super.init();
        getNetwork().registerMessage(ProjectTableCraftPacketMessageHandler.class, ProjectTableCraftPacket.class, 0, Side.CLIENT);

        exampleModKeyBinding = new KeyBinding("Start Project Table", Keyboard.KEY_P, "ExampleMod");
        ClientRegistry.registerKeyBinding(exampleModKeyBinding);
    }
}
