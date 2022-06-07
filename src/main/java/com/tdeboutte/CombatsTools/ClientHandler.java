package com.tdeboutte.CombatsTools;

import com.mojang.blaze3d.platform.InputConstants;
import com.tdeboutte.CombatsTools.waypoints.gui.WaypointSaveScreen;
import com.tdeboutte.CombatsTools.waypoints.gui.WaypointsListScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    private static KeyMapping keyAddWaypoint;
    private static KeyMapping keyListWaypoints;

    public static void init() {
//        MinecraftForge.EVENT_BUS.addListener(ClientHandler::clientTickEvent);
    }

    public static void initKeybinds() {
        ClientRegistry.registerKeyBinding(keyAddWaypoint = new KeyMapping(
                "key.combatstools.add_waypoint", InputConstants.UNKNOWN.getValue(), "category.combatstools" ));
        ClientRegistry.registerKeyBinding(keyListWaypoints = new KeyMapping(
                "key.combatstools.list_waypoints", InputConstants.UNKNOWN.getValue(), "category.combatstools" ));

        MinecraftForge.EVENT_BUS.addListener(ClientHandler::handleKeys);
    }

//    private static void clientTickEvent(TickEvent.ClientTickEvent event) {
//        LocalPlayer player = Minecraft.getInstance().player;
//        if (player != null) {
//            // POIs.onTick(player);
//        }
//    }

    private static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (keyAddWaypoint.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new WaypointSaveScreen(
                        mc.player.getBlockZ(),
                        mc.player.getBlockZ(),
                        mc.player.getBlockZ(),
                        mc.player.level.dimension().location().getPath())
                );
            }

            //noinspection StatementWithEmptyBody
            while (keyAddWaypoint.consumeClick());
        }
        if (keyListWaypoints.consumeClick()) {
            if (mc.screen == null) {
                mc.setScreen(new WaypointsListScreen());
            }

            //noinspection StatementWithEmptyBody
            while (keyListWaypoints.consumeClick());
        }
    }


}
