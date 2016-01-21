package com.example.examplemod;

import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by codew on 21/01/2016.
 */
public enum KeyboardHandler
{
    INSTANCE;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        // DEBUG


        // make local copy of key binding array
        KeyBinding keyBindings = ClientNetworkProxy.exampleModKeyBinding;

        if (keyBindings.isPressed()) {
            System.out.println("Key Input Event");
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            thePlayer.openGui(ExampleMod.instance, ModGuis.PROJECT_TABLE.getID(), thePlayer.worldObj, thePlayer.serverPosX, thePlayer.serverPosY, thePlayer.serverPosZ);
        }
    }
}
