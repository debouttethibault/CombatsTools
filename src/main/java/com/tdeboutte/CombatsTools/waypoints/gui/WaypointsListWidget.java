package com.tdeboutte.CombatsTools.waypoints.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tdeboutte.CombatsTools.waypoints.Waypoint;
import com.tdeboutte.CombatsTools.waypoints.WaypointDatabase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class WaypointsListWidget extends ObjectSelectionList<WaypointsListWidget.WaypointsListEntry> {

    class WaypointsListEntry extends ObjectSelectionList.Entry<WaypointsListEntry> {

        private final Font font;
        private final Waypoint waypoint;
        private final int listWidth;

        public WaypointsListEntry(@NonNull Waypoint waypoint, WaypointsListWidget parent) {
            this.waypoint = waypoint;
            this.font = parent.minecraft.font;
            this.listWidth = parent.listWidth;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", waypoint.name);
        }

        @Override
        public void render(@NotNull PoseStack matrixStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTick) {
            Component name = Component.literal(waypoint.name);
            Component coords = Component.literal(String.format("%s - %d / %d / %d", waypoint.dimension, waypoint.x, waypoint.y, waypoint.z));

            font.draw(matrixStack, Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(name, listWidth))), left + 3, top + 2, 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double p_94737_, double p_94738_, int p_94739_) {
            WaypointsListWidget.this.setSelected(this);
            return false;
        }
    }

    private final WaypointsListScreen parent;
    private final int listWidth;

    public WaypointsListWidget(WaypointsListScreen parent, int listWidth, int top, int bottom) {
        super(parent.getMinecraft(), listWidth, parent.height, top, bottom, parent.getMinecraft().font.lineHeight * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.refreshList();
    }

    @Override
    protected int getScrollbarPosition() {
        return this.listWidth;
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        WaypointDatabase.getWaypoints().values().stream()
                .map((wp) -> new WaypointsListEntry(wp, this)).forEach(this::addEntry);
    }

    @Override
    protected void renderBackground(@NotNull PoseStack matrixStack) {
        this.parent.renderBackground(matrixStack);
    }
}
