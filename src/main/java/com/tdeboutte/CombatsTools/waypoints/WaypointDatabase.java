package com.tdeboutte.CombatsTools.waypoints;

import com.tdeboutte.CombatsTools.CombatsTools;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.antlr.runtime.tree.Tree;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CombatsTools.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WaypointDatabase {
    private static final Logger LOGGER = LogManager.getLogger();

    private final static Map<String, Waypoint> waypoints = new TreeMap<>();

    public static boolean edit(int x, int y, int z, @NonNull String dimension, @NonNull String name, @NonNull String oldKey) {
        final String newKey = name.trim().toLowerCase(Locale.ROOT);
        if (!waypoints.containsKey(oldKey) || waypoints.containsKey(newKey))
            return false;
        waypoints.remove(oldKey);
        return save(new Waypoint(x, y, z, dimension, name));
    }

    public static boolean add(int x, int y, int z, @NonNull String dimension, @NonNull String name) {
        return save(new Waypoint(x, y, z, dimension, name));
    }

    private static boolean save(Waypoint waypoint) {
        final String key = waypoint.name.trim().toLowerCase(Locale.ROOT);
        if (waypoints.containsKey(key))
            return false;
        waypoints.put(key, waypoint);
        return true;
    }

    public static Map<String, Waypoint> getWaypoints() {
        return waypoints;
    }
}
