package com.tdeboutte.CombatsTools;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import com.tdeboutte.CombatsTools.waypoints.Waypoint;
import com.tdeboutte.CombatsTools.waypoints.WaypointDatabase;
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
import org.slf4j.Logger;

public class HudOverlay extends GuiComponent implements IIngameOverlay {
    private final static HudOverlay INSTANCE = new HudOverlay();
    private final static Logger LOGGER = LogUtils.getLogger();

    private final Minecraft mc;
    private final Font font;
    private final TextureManager textureManager;
    private final WaypointDatabase waypointDatabase;

    private boolean needsPop = false;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    private HudOverlay() {
        this.mc = Minecraft.getInstance();
        this.font = mc.font;
        this.textureManager = mc.textureManager;
        this.waypointDatabase = WaypointDatabase.getInstance();

        OverlayRegistry.registerOverlayBelow(ForgeIngameGui.BOSS_HEALTH_ELEMENT, "Combat's Tools", this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void preOverlayHigh(RenderGameOverlayEvent.PreLayer event) {
        if (event.getOverlay() == ForgeIngameGui.BOSS_HEALTH_ELEMENT && !mc.options.hideGui && canRender()) {
            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(0, 28, 0);
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
            event.getPoseStack().popPose();
            needsPop = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void postOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && needsPop) {
            PoseStack poseStack = event.getPoseStack();
            poseStack.popPose();
            needsPop = false;
        }
    }

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float _partialTicks, int width, int height) {
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
        String dir = getPlayerOrdinalDirection(yaw);
        if (dir.length() < 2) dir += " ";
        dir += String.format(" (%d??)", Mth.fastFloor(yaw));
        drawString(poseStack, font, dir, 3, 13, 0xffffffff);

        // Draw FPS
        // drawString(poseStack, font, "FPS: " + mc.fpsString.substring(0, mc.fpsString.indexOf(' ')), 3, 23, 0xffffffff);

        // Draw player coordinates
        int playerPosX = player.getBlockX();
        int playerPosY = player.getBlockY();
        int playerPosZ = player.getBlockZ();
        String pos = String.format("XYZ: %d / %d / %d", playerPosX, playerPosY, playerPosZ);
        // fill(poseStack, 2, 12, font.width(pos) + 4, 2, 0x3f000000);
        drawString(poseStack, font, pos, 3, 3, 0xffffffff);

        final Waypoint wp = waypointDatabase.waypointToTrack;
        if (wp != null) {
            int distanceToWaypoint = wp.getDistanceTo(playerPosX, playerPosY, playerPosZ);
//            int headingToWaypoint = wp.getHeadingTo(yaw, playerPosX, playerPosZ);
            drawCenteredString(poseStack, font, wp.name, width / 2, 3, 0xffffffff);
            drawCenteredString(poseStack, font, String.format("Distance: %d blocks", distanceToWaypoint), width / 2, 13, 0xffffffff);
//            drawCenteredString(poseStack, font, String.format("Head to: %d??", headingToWaypoint), width / 2, 23, 0xffffffff);
        }
    }

    private boolean canRender() {
        if (mc.player == null)
            return false;

        // Add config: always show, when item in hand...

        return true;
    }

    private static String getPlayerOrdinalDirection(float yaw) {
        // Find a way to make this better, maybe show compass?
        String dir;
        if (yaw > 337.5f || yaw <= 22.5f) {
            dir = "N";
        } else if (yaw > 22.5f && yaw <= 67.5f) {
            dir = "NE";
        } else if (yaw > 67.5f && yaw <= 112.5f) {
            dir = "E";
        } else if (yaw > 112.5f && yaw <= 157.5f) {
            dir = "SE";
        } else if (yaw > 157.5f && yaw <= 202.5f) {
            dir = "S";
        } else if (yaw > 202.5f && yaw <= 247.5f) {
            dir = "SW";
        } else if (yaw > 247.5f && yaw <= 292.5f) {
            dir = "W";
        } else {
            dir = "NW";
        }
        return dir;
    }

    public static HudOverlay getInstance() {
        return INSTANCE;
    }
}
