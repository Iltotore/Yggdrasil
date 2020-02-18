package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.item.InteractiveItemHandler;
import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellTier;
import net.akami.yggdrasil.api.spell.StorableSpellTier;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell<EarthTowerLauncher> {

    private InteractiveItemHandler handler;

    public EarthTowerSpell(InteractiveItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<SpellTier<EarthTowerLauncher>> getTiers() {
        return Arrays.asList(new SpellTimeTier<>(100), new SpellRadiusTier<>(1), new SpellSpeedTier<>(1), new StorableSpellTier<>(ItemStack.of(ItemTypes.GRASS), handler, 3), new EarthTowerSizeTier(20), new SpellRadiusTier<>(2));
    }

    @Override
    public EarthTowerLauncher getLauncher() {
        return new EarthTowerLauncher();
    }
}
