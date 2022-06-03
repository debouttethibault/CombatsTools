package com.tdeboutte.CombatsTools;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

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
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ClientHandler.initKeybinds();
            HudOverlay.init();
        });
    }
}
