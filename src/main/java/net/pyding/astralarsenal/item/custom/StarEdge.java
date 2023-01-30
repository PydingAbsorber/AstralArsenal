package net.pyding.astralarsenal.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.constellation.*;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeGenItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;
import hellfirepvp.astralsorcery.common.item.tool.CrystalToolTier;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;
import java.util.Properties;

import static hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas.CONFIG;

public class StarEdge extends Item implements CrystalAttributeItem, TypeEnchantableItem, ConstellationItem {

    public StarEdge(Properties properties) {
        super(properties);
    }
    private double getAttackSpeed() {
        return -2.4;
    }

    private double getAttackDamage(ItemStack stack) {
        return 14;
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.getAttackSpeed(), AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        EnchantmentType type = enchantment.type;
        return type == EnchantmentType.WEAPON || type == EnchantmentType.BREAKABLE;
    }

    @Nullable
    @Override
    public CrystalAttributes getAttributes(ItemStack itemStack) {
        return CrystalAttributes.getCrystalAttributes(itemStack);
    }

    @Override
    public void setAttributes(ItemStack stack, @Nullable CrystalAttributes attributes) {
        if (attributes != null) {
            attributes.store(stack);
        } else {
            CrystalAttributes.storeNull(stack);
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack, EnchantmentType enchantmentType) {
        return true;
    }

    @Nullable
    @Override
    public IWeakConstellation getAttunedConstellation(ItemStack itemStack) {
        return (IWeakConstellation) IConstellation.readFromNBT(NBTHelper.getPersistentData(itemStack));
    }

    @Override
    public boolean setAttunedConstellation(ItemStack stack, @Nullable IWeakConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack));
        } else {
            NBTHelper.getPersistentData(stack).remove(IConstellation.getDefaultSaveKey());
        }
        return true;
    }

    @Nullable
    @Override
    public IMinorConstellation getTraitConstellation(ItemStack stack) {
        return (IMinorConstellation) IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), "constellationTrait");
    }

    @Override
    public boolean setTraitConstellation(ItemStack stack, @Nullable IMinorConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), "constellationTrait");
        } else {
            NBTHelper.getPersistentData(stack).remove("constellationTrait");
        }
        return true;
    }
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        PlayerEntity player = (PlayerEntity) attacker;
        IConstellation constellation = getAttunedConstellation(stack);
        if(constellation.getConstellationName().equals("Aevitas"))
        {
            stack.getOrCreateTag().putDouble("aevitasstacktime", 100);
            Double victim = stack.getOrCreateTag().getDouble("victim");
            Double victim2 = stack.getOrCreateTag().getDouble("victim2");
            stack.getOrCreateTag().putDouble("victim1", target.getUniqueID().getMostSignificantBits());
            if(victim2 == null)
                stack.getOrCreateTag().putDouble("victim2", victim);
            if(victim2 == victim) {
                if(target.getPersistentData().getDouble("aevitasstack") < 20)
                player.getPersistentData().putDouble("aevitasstack", target.getPersistentData().getDouble("aevitasstack") + 1);
            } else
            {
                player.getPersistentData().putDouble("aevitasstack", 0);
            }



        }
        if(constellation.getConstellationName().equals("Evorsio"))
            stack.getOrCreateTag().putDouble("evorsio",1);
        else
            stack.getOrCreateTag().putDouble("evorsio",0);
        if(target instanceof PlayerEntity && constellation.getConstellationName().equals("Vicio")) {
            PlayerEntity entity = (PlayerEntity) target;
            AlignmentChargeHandler.INSTANCE.drainCharge(entity, LogicalSide.SERVER, 100, false);
        }
        if(constellation.getConstellationName().equals("Gelu"))
            stack.getOrCreateTag().putDouble("gelu" , 1);
        else
            stack.getOrCreateTag().putDouble("gelu" , 0);
        if(constellation.getConstellationName().equals("Armara"))
            stack.getOrCreateTag().putDouble("armara" , 1);
            else
            stack.getOrCreateTag().putDouble("armara" , 0);
        if(constellation.getConstellationName().equals("Decidia")) {
            stack.getOrCreateTag().putDouble("decidia" , 1);
            //target.attackEntityFrom(new DamageSource("astral").setDamageIsAbsolute().setDamageBypassesArmor(),6+EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack));
        } else
            stack.getOrCreateTag().putDouble("decidia" , 0);
        if(constellation.getConstellationName().equals("Armara"))
            player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 200, 3));
        return super.hitEntity(stack, target, attacker);
    }
}
