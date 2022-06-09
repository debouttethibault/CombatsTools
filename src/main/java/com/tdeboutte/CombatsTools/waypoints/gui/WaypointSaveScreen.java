package com.tdeboutte.CombatsTools.waypoints.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tdeboutte.CombatsTools.waypoints.WaypointDatabase;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WaypointSaveScreen extends Screen {

    private final int x;
    private final int y;
    private final int z;
    private final String dimension;
    private String name;

    private EditBox editBoxName;

    public WaypointSaveScreen(int x, int y, int z, @NonNull String dimension) {
        super(Component.literal("Save waypoint"));

        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    @Override
    protected void init() {
        super.init();

        assert minecraft != null;
        Font font = this.minecraft.font;

        int width_div_2 = this.width / 2;

        editBoxName = new EditBox(font, width_div_2 - 100, this.height / 2 - 35, 174, 18, Component.literal("Name"));
        editBoxName.setValue(getInitialName());
        this.addRenderableWidget(editBoxName);

        // x y width height
        EditBox editBoxX = new EditBox(font, width_div_2 - 100, this.height / 2 - 9, 61, 18, Component.literal("X"));
        EditBox editBoxY = new EditBox(font, width_div_2 - 32, this.height / 2 - 9, 62, 18, Component.literal("Y"));
        EditBox editBoxZ = new EditBox(font, width_div_2 + 39, this.height / 2 - 9, 61, 18, Component.literal("Z"));
        editBoxX.setValue(String.valueOf(x));
        editBoxY.setValue(String.valueOf(y));
        editBoxZ.setValue(String.valueOf(z));
        editBoxX.setEditable(false);
        editBoxY.setEditable(false);
        editBoxZ.setEditable(false);
        this.addRenderableWidget(editBoxX);
        this.addRenderableWidget(editBoxY);
        this.addRenderableWidget(editBoxZ);

        Button saveButton = new Button(width_div_2 - 102, this.height / 2 + 17, 98, 20, Component.literal("Save"), this::onPressSave);
        this.addRenderableWidget(saveButton);

        Button closeButton = new Button(width_div_2 + 4, this.height / 2 + 17, 98, 20, Component.literal("Cancel"), this::onPressClose);
        this.addRenderableWidget(closeButton);
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

    private void onPressSave(@NonNull Button saveButton) {
        assert editBoxName != null;
        if (editBoxName.getValue().isBlank())
            return;

        final String name = editBoxName.getValue().trim();
        if (WaypointDatabase.add(this.x, this.y, this.z, this.dimension, name))
            close();
    }

    private void onPressClose(@NonNull Button closeButton) {
        close();
    }

    private void close() {
        assert this.minecraft != null;
        this.minecraft.setScreen(null);
    }

    private String getInitialName() {
        return "WP " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
    }
}
