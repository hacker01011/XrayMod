package com.xray;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.lwjgl.glfw.GLFW;

import java.util.Set;

public class XrayMod implements ClientModInitializer {
    public static boolean xrayEnabled = false;
    public static Set<Block> oreBlocks;

    private static KeyMapping toggleKeyMapping;

    @Override
    public void onInitializeClient() {
        // Initialized here so Blocks.* fields are populated (Bootstrap is done)
        oreBlocks = Set.of(
            Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
            Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
            Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE,
            Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.NETHER_QUARTZ_ORE,
            Blocks.ANCIENT_DEBRIS
        );

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("xraymod", "general")
        );

        toggleKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.xraymod.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeyMapping.consumeClick()) {
                xrayEnabled = !xrayEnabled;
                if (client.player != null) {
                    client.player.sendOverlayMessage(
                        Component.literal("X-Ray: " + (xrayEnabled ? "§aEnabled" : "§cDisabled"))
                    );
                }
                client.levelExtractor.allChanged();
            }
        });
    }
}
