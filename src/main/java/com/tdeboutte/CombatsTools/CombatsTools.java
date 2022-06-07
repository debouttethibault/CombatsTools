package com.tdeboutte.CombatsTools;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CombatsTools.MODID)
public class CombatsTools {
    public static final String MODID = "combatstools";

    private static final Logger LOGGER = LogUtils.getLogger();

    public CombatsTools() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::loadComplete);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Hello from Combat's Tools!");
    }

    private void clientSetup(FMLClientSetupEvent event) {
        ClientHandler.init();
    }

    private void loadComplete(FMLLoadCompleteEvent event) {
        //noinspection deprecation
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ClientHandler.initKeybinds();
            HudOverlay.init();
        });
    }
}
