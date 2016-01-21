package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacket;
import com.example.examplemod.ProjectTableExample.ProjectTableCraftPacketMessageHandler;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by codew on 17/01/2016.
 */
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
