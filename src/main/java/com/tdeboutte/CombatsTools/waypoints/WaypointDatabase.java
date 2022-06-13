package com.tdeboutte.CombatsTools.waypoints;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WaypointDatabase {
    private static final Logger LOGGER = LogManager.getLogger();
    private final static WaypointDatabase INSTANCE = new WaypointDatabase();

    private final Map<String, Waypoint> waypoints;

    @Nullable
    public Waypoint waypointToTrack;

    private WaypointDatabase() {
        waypoints = new TreeMap<>();
    }

    public boolean edit(int x, int y, int z, @NotNull String dimension, @NotNull String name, @NotNull String oldKey) {
        final String newKey = name.trim().toLowerCase(Locale.ROOT);
        if (!waypoints.containsKey(oldKey) || waypoints.containsKey(newKey))
            return false;
        waypoints.remove(oldKey);
        return save(new Waypoint(x, y, z, dimension, name));
    }

    public boolean add(int x, int y, int z, @NotNull String dimension, @NotNull String name) {
        return save(new Waypoint(x, y, z, dimension, name));
    }

    private boolean save(Waypoint waypoint) {
        final String key = waypoint.name.trim().toLowerCase(Locale.ROOT);
        if (waypoints.containsKey(key))
            return false;

        waypoints.put(key, waypoint);
        return true;
    }

    public Map<String, Waypoint> getWaypoints() {
        return waypoints;
    }

    public Stream<Waypoint> getWaypointsStream() {
        return waypoints.values().stream();
    }

    private static void debug(Map<String, Waypoint> waypoints) {
        StringBuilder msg = new StringBuilder("WAYPOINTS: \n");
        for (Waypoint wp : waypoints.values()) {
            msg.append("\t - ").append(wp.name).append("\n");
        }
        LOGGER.debug(msg);
    }

    // Why xml? Because I'm lazy and I don't want to write a serializer for the waypoints.
    public void persistToDisk(@NotNull String filePath) {
        LOGGER.info("Persisting waypoints to {}", filePath);

        try (XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            xmlEncoder.writeObject(waypoints.values());
        } catch (Exception e) {
            LOGGER.error("Failed to persist waypoints to {}", filePath, e);
        }
    }

    public void loadFromDisk(@NotNull String filePath) {
        LOGGER.info("Loading waypoints from {}", filePath);

        waypoints.clear();
        try (XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)))) {
            //noinspection unchecked
            Collection<Waypoint> wps = (Collection<Waypoint>) xmlDecoder.readObject();
            waypoints.putAll(wps.stream().collect(Collectors.toMap(Waypoint::getName, wp -> wp)));
        } catch (Exception e) {
            LOGGER.error("Failed to load waypoints from {}", filePath, e);
        }
    }

    public static WaypointDatabase getInstance() {
        return INSTANCE;
    }
}
