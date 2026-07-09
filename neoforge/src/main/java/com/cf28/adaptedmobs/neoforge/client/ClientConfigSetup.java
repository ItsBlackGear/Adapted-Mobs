package com.cf28.adaptedmobs.neoforge.client;

import com.cf28.adaptedmobs.client.config.AMConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientConfigSetup {
    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (c, parent) -> AMConfigScreen.create(parent));
    }
}
