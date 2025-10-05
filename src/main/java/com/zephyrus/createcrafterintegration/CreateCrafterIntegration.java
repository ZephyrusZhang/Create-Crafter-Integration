package com.zephyrus.createcrafterintegration;

import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;

@Mod(CreateCrafterIntegration.MODID)
public class CreateCrafterIntegration {

    public static final String MODID = "create_crafter_integration";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateCrafterIntegration(IEventBus eventBus) {
        eventBus.register(new HandlerRegistry());
    }

    public static class HandlerRegistry {

        @SubscribeEvent
        public void setup(final FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                BlockEntityType.CRAFTER.getValidBlocks().forEach(block -> UnpackingHandler.REGISTRY.register(block, new CrafterUnpackingHandler()));
            });
        }

    }

}
