package com.jkdr.abyssalascentdimensionpatcher.mixins;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.jkdr.abyssalascentdimensionpatcher.config.IronSpellbookExpandedConfig;
import com.jkdr.abyssalascentdimensionpatcher.config.IronSpellbookExpandedConfig.SpellConfigEntries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

// 1. Target the class containing the method
@Mixin(value = MagicManager.class, remap = false)
public abstract class MixinSpellCooldowns {

    @Inject(
        method = "getEffectiveSpellCooldown",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void addCustomCooldown(AbstractSpell spell, Player player, CastSource castSource, CallbackInfoReturnable<Integer> cir) {
        String spellId = spell.getSpellId();
        SpellConfigEntries configEntries = IronSpellbookExpandedConfig.SPELL_ADDED_CONFIGS.get(spellId);
        int originalCooldown = cir.getReturnValue();

        int addedTicks = 0;

        if (configEntries != null) {
            double addedCooldownSeconds = configEntries.baseCooldownInSeconds().get();

            addedTicks = (int) (addedCooldownSeconds * 20.0);
        }
        
        cir.setReturnValue(originalCooldown + addedTicks);
    }
}