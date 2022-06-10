package com.tdeboutte.CombatsTools.waypoints;

import org.jetbrains.annotations.NotNull;

public class Waypoint {
    public int x;
    public int y;
    public int z;
    public String dimension;
    public String name;

    public Waypoint(int x, int y, int z, @NotNull String dim, @NotNull String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", dimension='" + dimension + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
