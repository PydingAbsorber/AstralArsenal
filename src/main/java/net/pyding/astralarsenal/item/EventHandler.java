package net.pyding.astralarsenal.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.AbsorptionEffect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pyding.astralarsenal.item.custom.StarEdge;

public class EventHandler {
    @Mod.EventBusSubscriber
    private static class GlobalTrigger {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void PlayerTickUpdate(TickEvent.PlayerTickEvent event)
        {
            PlayerEntity player = (PlayerEntity) event.player;
            if(player.getPersistentData().getDouble("aevitasstacktime") > 0)
            player.getPersistentData().putDouble("aevitasstacktime", player.getPersistentData().getDouble("aevitasstacktime")-1);
            else player.getPersistentData().putDouble("aevitasstack",0);
        }
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void CriticalHit(CriticalHitEvent event)
        {
            Entity entity = event.getEntity();
            entity.getPersistentData().putDouble("crit", 1);
        }
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onEntityAttacked(LivingHurtEvent event)
        {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                Entity sourceentity = event.getSource().getTrueSource();
                Entity livingEntity = event.getEntityLiving();
                if(sourceentity != null) {
                    ItemStack itemhand = ((sourceentity instanceof LivingEntity) ? ((LivingEntity) sourceentity).getHeldItemOffhand() : ItemStack.EMPTY);
                    if (event.getSource().isProjectile() && sourceentity != null)
                        sourceentity.getPersistentData().putDouble("Bowshot", 1);
                    if (event.getSource() == DamageSource.causeTridentDamage(sourceentity, entity))
                        sourceentity.getPersistentData().putDouble("trident", 10);

                    if (itemhand.getItem().equals(ModItems.STAR_EDGE)) {
                        }
                        if (itemhand.getOrCreateTag().getDouble("Aevitas") == 1 && entity instanceof PlayerEntity) {
                            PlayerEntity player = (PlayerEntity) event.getEntity();
                            player.removePotionEffect(Effects.ABSORPTION);
                            player.removePotionEffect(Effects.REGENERATION);
                            ItemStack items = sourceentity.getEquipmentAndArmor().iterator().next();
                            items.setDamage((int) (items.getDamage()-10));
                        }
                        if (itemhand.getOrCreateTag().getDouble("Evorsio") == 1) {
                            ItemStack items = entity.getEquipmentAndArmor().iterator().next();
                            items.setDamage((int) (items.getDamage()+40));
                        }
                    if (itemhand.getOrCreateTag().getDouble("Vicio") == 1 && sourceentity.getPersistentData().getDouble("trident") == 1) {
                        sourceentity.getPersistentData().putDouble("trident", sourceentity.getPersistentData().getDouble("tridemt")-1);
                        if(sourceentity.getPersistentData().getDouble("VicioStack") < 10)
                        sourceentity.getPersistentData().putDouble("VicioStack",sourceentity.getPersistentData().getDouble("VicioStack")+1);
                    }

                        if (itemhand.getOrCreateTag().getDouble("Armara") == 1) {
                            if (Math.random() < 0.1 && sourceentity.getPersistentData().getDouble("crit") == 0)
                                event.setAmount(event.getAmount() * 10);
                        }
                        if (itemhand.getOrCreateTag().getDouble("Gelu") == 1) {
                        if (Math.random() < 0.5 && sourceentity.getPersistentData().getDouble("crit") == 1)
                            event.setAmount(event.getAmount() * (float) 2.5);
                        if (sourceentity.getPersistentData().getDouble("Bowshot") > 0 && itemhand.getOrCreateTag().getDouble("Alcara") == 1) {
                            event.setAmount(event.getAmount() * (float) 7);
                            sourceentity.getPersistentData().putDouble("Bowshot", 0);
                        }
                    }
                }
            }
        }
        @SubscribeEvent(priority = EventPriority.LOWEST , receiveCanceled = true)
        public static void onEntityAttacked2(LivingHurtEvent event) {
            if (event != null && event.getEntity() != null) {
                Entity entity = event.getEntity();
                Entity sourceentity = event.getSource().getTrueSource();
                Entity livingEntity = event.getEntityLiving();
                if(sourceentity!= null) {
                    ItemStack itemhand = ((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHeldItemOffhand() : ItemStack.EMPTY);
                    if (itemhand.getItem().equals(ModItems.STAR_EDGE) && itemhand.getOrCreateTag().getDouble("Decidia") == 1) {
                        event.setCanceled(false);
                        event.getSource().setDamageBypassesArmor().setDamageIsAbsolute();
                        event.setAmount(event.getAmount() + 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemhand.getStack()));
                    }
                    sourceentity.getPersistentData().putDouble("nocrit", 0);
                }
            }
        }
    }
}
