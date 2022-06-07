package com.tdeboutte.CombatsTools.waypoints;

import net.minecraft.client.gui.components.ObjectSelectionList;

public class Waypoint {
    public int x;
    public int y;
    public int z;
    public String dimension;
    public String name;

    public Waypoint(int x, int y, int z, String dim, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
        this.name = name;
    }

    public Waypoint(int x, int y, int z, String dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dim;
        this.name = "";
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
