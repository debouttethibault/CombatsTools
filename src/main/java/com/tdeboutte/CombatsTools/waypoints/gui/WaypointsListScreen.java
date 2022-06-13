package com.tdeboutte.CombatsTools.waypoints.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tdeboutte.CombatsTools.waypoints.WaypointDatabase;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class WaypointsListScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();

    private WaypointDatabase waypointDatabase;

    private final int x;
    private final int y;
    private final int z;
    private final String dimension;

    public WaypointsListScreen(int x, int y, int z, @NotNull String dimension)  {
        super(Component.literal("Waypoints"));

        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    @Override
    protected void init() {
        super.init();

        waypointDatabase = WaypointDatabase.getInstance();

        LOGGER.debug(String.format("W: %d, H: %d", this.width, this.height));

        if (waypointDatabase.getWaypoints().size() > 0) {
            AtomicInteger wpi = new AtomicInteger();
            waypointDatabase.getWaypointsStream()
                    .forEach((wp) -> {
                        this.addRenderableWidget(new Button(this.width / 2 - 100, ((wpi.getAndIncrement() + 3) * 16) + 3, 200, 16, Component.literal(wp.name), (btn) -> {
                            waypointDatabase.waypointToTrack = wp;
                            this.close();
                        }));
                    });
        } else {
            this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 2 - 8, 120, 16, Component.literal("No waypoints"), (btn) -> {
                waypointDatabase.waypointToTrack = null;
                this.close();
            }));
        }

    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void close() {
        assert this.minecraft != null;
        this.minecraft.setScreen(null);
    }
}
