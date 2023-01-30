package net.pyding.astralarsenal.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
                ItemStack itemhand = ((sourceentity instanceof LivingEntity) ? ((LivingEntity) sourceentity).getHeldItemOffhand() : ItemStack.EMPTY);
                if(event.getSource().isProjectile() && sourceentity != null)
                    sourceentity.getPersistentData().putDouble("bowshot",1);
                if(itemhand.getItem().equals(ModItems.STAR_EDGE))
                {
                    if (itemhand.getOrCreateTag().getDouble("armara") == 1) {
                        if (Math.random() < 0.1 && sourceentity.getPersistentData().getDouble("crit") == 0)
                            event.setAmount(event.getAmount() * 10);
                    }
                    if (itemhand.getOrCreateTag().getDouble("gelu") == 1) {
                        if (Math.random() < 0.5 && sourceentity.getPersistentData().getDouble("crit") == 1)
                            event.setAmount(event.getAmount() * (float)2.5);
                    }
                    if (itemhand.getOrCreateTag().getDouble("aevitas") == 1)
                    {
                        event.setAmount(event.getAmount()*(float)(1+0.06*sourceentity.getPersistentData().getDouble("aevitasstack")));
                    }
                    if(sourceentity.getPersistentData().getDouble("bowshot") > 0 && itemhand.getOrCreateTag().getDouble("evorsio") == 1)
                    {
                        event.setAmount(event.getAmount()*(float)2.5);
                        sourceentity.getPersistentData().putDouble("bowshot",0);
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
                ItemStack itemhand = ((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHeldItemOffhand() : ItemStack.EMPTY);
                if(itemhand.getItem().equals(ModItems.STAR_EDGE) && itemhand.getOrCreateTag().getDouble("decidia") == 1) {
                    event.setCanceled(false);
                    event.getSource().setDamageBypassesArmor().setDamageIsAbsolute();
                    event.setAmount(event.getAmount() + 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemhand.getStack()));
                }
                sourceentity.getPersistentData().putDouble("nocrit" , 0);
            }
        }
    }
}
