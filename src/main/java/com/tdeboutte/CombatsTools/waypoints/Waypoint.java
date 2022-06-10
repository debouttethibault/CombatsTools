package com.tdeboutte.CombatsTools.waypoints;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
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

    public int getDistanceTo(int x, int y, int z) {
        return (int) Mth.sqrt(Mth.square(this.x - x) + Mth.square(this.y - y) + Mth.square(this.z - z));
    }

/*    public int getHeadingTo(float yaw, int x, int z) {
        yaw += 90.0F;
        Vec2 p = new Vec2(Mth.cos(yaw * Mth.DEG_TO_RAD), Mth.sin(yaw * Mth.DEG_TO_RAD));
        Vec2 p_wp = new Vec2(x - this.x, z - this.z).normalized();
        float dot = p.dot(p_wp);
        return (int) ((Math.acos(dot) + Mth.PI) * Mth.RAD_TO_DEG);
    }*/
}
