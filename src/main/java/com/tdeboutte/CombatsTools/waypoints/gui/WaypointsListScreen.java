package com.tdeboutte.CombatsTools.waypoints.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class WaypointsListScreen extends Screen {
    public WaypointsListScreen() {
        super(new TextComponent("Waypoints"));
    }

    @Override
    protected void init() {
        super.init();

        WaypointsListWidget waypointsListWidget = new WaypointsListWidget(this, 250, 8, 8);
        this.addRenderableWidget(waypointsListWidget);
    }

    @Override
    public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
