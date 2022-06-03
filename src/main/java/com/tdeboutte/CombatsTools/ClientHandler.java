package com.tdeboutte.CombatsTools;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public class ClientHandler {
    private static KeyMapping keyAddWaypoint;
    private static KeyMapping keyRemoveWaypoint;
    private static KeyMapping keyEditWaypoint;

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ClientHandler::clientTickEvent);
    }

    public static void initKeybinds() {
        ClientRegistry.registerKeyBinding(keyAddWaypoint = new KeyMapping(
                "key.combatstools.add_waypoint", InputConstants.UNKNOWN.getValue(), "category.combatstools" ));
        ClientRegistry.registerKeyBinding(keyRemoveWaypoint = new KeyMapping(
                "key.combatstools.remove_waypoint", InputConstants.UNKNOWN.getValue(), "category.combatstools" ));
        ClientRegistry.registerKeyBinding(keyEditWaypoint = new KeyMapping(
                "key.combatstools.edit_waypoint", InputConstants.UNKNOWN.getValue(), "category.combatstools" ));

        MinecraftForge.EVENT_BUS.addListener(ClientHandler::handleKeys);
    }

    private static void clientTickEvent(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            // POIs.onTick(player);
        }
    }

    private static void handleKeys(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if (keyAddWaypoint.consumeClick()) {}
        if (keyEditWaypoint.consumeClick()) {}
        if (keyRemoveWaypoint.consumeClick()) {}
    }


}
