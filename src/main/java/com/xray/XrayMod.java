package com.xray;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class XrayMod implements ClientModInitializer {
    public static boolean xrayEnabled = false;
    private static KeyMapping toggleKeyMapping;

    @Override
    public void onInitializeClient() {
        // Register custom category using ResourceLocation
        KeyMapping.Category category = KeyMapping.Category.register(
            ResourceLocation.fromNamespaceAndPath("xraymod", "general")
        );

        // Register KeyMapping
        toggleKeyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.xraymod.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            category
        ));

        // Listen for client ticks
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKeyMapping.consumeClick()) {
                xrayEnabled = !xrayEnabled;
                if (client.player != null) {
                    // Send status message to action bar overlay (true)
                    client.player.displayClientMessage(
                        Component.literal("X-Ray: " + (xrayEnabled ? "§aEnabled" : "§cDisabled")), 
                        true
                    );
                }
                if (client.levelRenderer != null) {
                    client.levelRenderer.allChanged();
                }
            }
        });
    }
}
