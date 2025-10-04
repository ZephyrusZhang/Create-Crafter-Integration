package com.zephyrus.createcrafterintegration;


import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(CreateCrafterIntegration.MODID)
public class Registry {

    public Registry(IEventBus bus) {
        bus.register(new Common());
    }

    public static class Common {

        @SubscribeEvent
        public void setup(final FMLCommonSetupEvent event) {
            event.enqueueWork(this::registerUnpackingHandler);
        }

        private void registerUnpackingHandler() {
            BlockEntityType.CRAFTER.getValidBlocks().forEach(block -> UnpackingHandler.REGISTRY.register(block, new CrafterUnpackingHandler()));
        }

    }

}
