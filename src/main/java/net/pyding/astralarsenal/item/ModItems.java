package net.pyding.astralarsenal.item;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.pyding.astralarsenal.AstralArsenalMod;
import net.pyding.astralarsenal.item.custom.StarEdge;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "astralarsenal");

    public static final RegistryObject<Item> STAR_EDGE = ITEMS.register("star_edge",
            () -> new StarEdge(new Item.Properties().group(AstralArsenalMod.ASTRALARSENAL_GROUP).maxDamage(1000).isImmuneToFire()));
    public static final RegistryObject<Item> TAB_ITEM = ITEMS.register("tab_item",
            () -> new Item(new Item.Properties().isImmuneToFire().maxStackSize(1).group(null)));




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
