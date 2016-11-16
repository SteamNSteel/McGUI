package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum KeyboardHandler
{
    INSTANCE;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onEvent(KeyInputEvent event)
    {
        // DEBUG


        // make local copy of key binding array
        KeyBinding keyBindings = ClientNetworkProxy.exampleModKeyBinding;

        if (keyBindings.isPressed()) {
            System.out.println("Key Input Event");
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().player;
            thePlayer.openGui(ExampleMod.instance, ModGuis.PROJECT_TABLE.getID(), thePlayer.world, (int)thePlayer.posX, (int)thePlayer.posY, (int)thePlayer.posZ);
        }
    }
}
