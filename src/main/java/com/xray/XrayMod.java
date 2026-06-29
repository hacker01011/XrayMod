package com.xray;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class XrayMod implements ClientModInitializer {
    public static boolean xrayEnabled = false;
    private static KeyMapping toggleKeyMapping;

    @Override
    public void onInitializeClient() {
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
                if (client.levelRenderer != null) {
                    client.levelRenderer.clearVisibleSections();
                }
            }
        });
    }
}
