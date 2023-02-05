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
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import org.antlr.v4.runtime.misc.NotNull;

import javax.annotation.Nullable;
import java.util.Properties;

import static hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas.CONFIG;

public class StarEdge extends Item implements CrystalAttributeItem, TypeEnchantableItem, ConstellationBaseItem {

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
    public IConstellation getConstellation(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), IConstellation.getDefaultSaveKey());
    }

    @Override
    public boolean setConstellation(ItemStack stack, @Nullable IConstellation cst) {
        if (stack.isEmpty()) {
            return false;
        }
        if (cst == null) {
            NBTHelper.getPersistentData(stack).remove(IConstellation.getDefaultSaveKey());
        } else {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), IConstellation.getDefaultSaveKey());
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        PlayerEntity player = (PlayerEntity) entityIn;
        if (isSelected == true)
        {
            String[] constallations = new String[16];
            constallations[0] = "Aevitas";
            constallations[1] = "Evorsio";
            constallations[2] = "Vicio";
            constallations[3] = "Decidia";
            constallations[4] = "Armara";
            constallations[5] = "Horologium";
            constallations[6] = "Lucerna";
            constallations[7] = "Mineralis";
            constallations[8] = "Octans";
            constallations[9] = "Bootes";
            constallations[10] = "Fornax";
            constallations[11] = "Pelotrio";
            constallations[12] = "Gelu";
            constallations[13] = "Ulteria";
            constallations[14] = "Alcara";
            constallations[15] = "Vorux";
            IConstellation cst = this.getConstellation(stack);
            if(cst != null) {
                for (int i = 0; i < 15; i++) {
                    if (cst.getConstellationName().equals(constallations[i]))
                        stack.getOrCreateTag().putDouble(constallations[i], 1);
                    else
                        stack.getOrCreateTag().putDouble(constallations[i], 0);
                }
            }
        }
    }
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        PlayerEntity player = (PlayerEntity) attacker;
        IConstellation cst = this.getConstellation(stack);
        if(target instanceof PlayerEntity && cst.getConstellationName().equals("Lucerna")) {
            PlayerEntity entity = (PlayerEntity) target;
            AlignmentChargeHandler.INSTANCE.drainCharge(entity, LogicalSide.SERVER, 100, false);
        }


        return super.hitEntity(stack, target, attacker);
    }


}
