package com.cf28.adaptedmobs.fabric.client;

import com.cf28.adaptedmobs.client.config.AMConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AMConfigScreen::create;
    }
}
