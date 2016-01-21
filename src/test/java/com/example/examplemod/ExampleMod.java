package com.example.examplemod;

import com.sun.webpane.webkit.dom.KeyboardEventImpl;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public static final String VERSION = "1.0";

    @SuppressWarnings({"StaticVariableOfConcreteClass", "StaticNonFinalField", "PublicField", "StaticVariableMayNotBeInitialized"})
    @Mod.Instance
    public static ExampleMod instance;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, GuiHandler.INSTANCE);
        Proxies.network.init();
        MinecraftForge.EVENT_BUS.register(KeyboardHandler.INSTANCE);
    }
}
