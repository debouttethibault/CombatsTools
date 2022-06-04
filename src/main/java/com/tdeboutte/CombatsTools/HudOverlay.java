package com.tdeboutte.CombatsTools;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HudOverlay extends GuiComponent implements IIngameOverlay {
    private final static HudOverlay INSTANCE = new HudOverlay();

    private final Minecraft mc;
    private final Font font;
    private final TextureManager textureManager;

    private boolean needsPop = false;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    private HudOverlay() {
        this.mc = Minecraft.getInstance();
        this.font = mc.font;
        this.textureManager = mc.textureManager;

        OverlayRegistry.registerOverlayBelow(ForgeIngameGui.BOSS_HEALTH_ELEMENT, "Combat's Tools", this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void preOverlayHigh(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.BOSS_HEALTH_ELEMENT && !mc.options.hideGui && canRender()) {
            PoseStack matrixStack = event.getMatrixStack();
            matrixStack.pushPose();
            matrixStack.translate(0, 28, 0);
            needsPop = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void preOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            needsPop = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void postOverlay(RenderGameOverlayEvent.PostLayer event)
    {
        if (event.getOverlay() == ForgeIngameGui.BOSS_HEALTH_ELEMENT && needsPop) {
            event.getMatrixStack().popPose();
            needsPop = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void postOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && needsPop) {
            PoseStack matrixStack = event.getMatrixStack();
            matrixStack.popPose();
            needsPop = false;
        }
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack matrixStack, float _partialTicks, int width, int height) {
        if (!canRender())
            return;

        boolean isPaused = mc.isPaused();
        float elapsed = isPaused ? 0 : mc.getDeltaFrameTime();
        float partialTicks = isPaused ? 0 : _partialTicks;

        final Player player = mc.player;
        assert (player != null);

        RenderSystem.enableBlend();

        // Draw player direction
        float yaw = Mth.lerp(partialTicks, player.yHeadRotO, player.yHeadRot) % 360;
        if (yaw < 0) yaw += 360;
        String dir = getPlayerDirectionString(yaw);
        drawString(matrixStack, font, dir, 3, 13, 0xffffffff);

        // Draw FPS
        // drawString(matrixStack, font, "FPS: " + mc.fpsString.substring(0, mc.fpsString.indexOf(' ')), 3, 23, 0xffffffff);

        // Draw player coordinates
        double playerPosX = Mth.lerp(partialTicks, player.xo, player.getX());
        double playerPosY = Mth.lerp(partialTicks, player.yo, player.getY());
        double playerPosZ = Mth.lerp(partialTicks, player.zo, player.getZ());
        String pos = String.format("XYZ: %4.1f / %4.1f / %4.1f", playerPosX, playerPosY, playerPosZ);
        // fill(matrixStack, 2, 12, font.width(pos) + 4, 2, 0x3f000000);
        drawString(matrixStack, font, pos, 3, 3, 0xffffffff);
    }

    private boolean canRender() {
        if (mc.player == null)
            return false;

        // Add config: always show, when item in hand...

        return true;
    }

    private static String getPlayerDirectionString(float yaw) {
        // Find a way to make this better, maybe show compass?
        String dir;
        if (yaw > 337.5f || yaw <= 22.5f) {
            dir = "N ";
        } else if (yaw > 22.5f && yaw <= 67.5f) {
            dir = "NE";
        } else if (yaw > 67.5f && yaw <= 112.5f) {
            dir = "E ";
        } else if (yaw > 112.5f && yaw <= 157.5f) {
            dir = "SE";
        } else if (yaw > 157.5f && yaw <= 202.5f) {
            dir = "S ";
        } else if (yaw > 202.5f && yaw <= 247.5f) {
            dir = "SW";
        } else if (yaw > 247.5f && yaw <= 292.5f) {
            dir = "W ";
        } else {
            dir = "NW";
        }
        dir += String.format(" (%d°)", Mth.fastFloor(yaw));
        return dir;
    }
}
