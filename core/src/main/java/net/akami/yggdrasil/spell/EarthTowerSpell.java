package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.Spell;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.world.schematic.Schematic;

import java.util.Arrays;
import java.util.List;

public class EarthTowerSpell implements Spell {

    private Schematic towerSchematic;

    public EarthTowerSpell(Schematic towerSchematic) {
        this.towerSchematic = towerSchematic;
    }

    private List<SpellTier> loadTiers() {
        return Arrays.asList();
    }

    @Override
    public List<SpellTier> getTiers() {
        SpellTier tier = (caster, data) -> {

        };
        return Arrays.asList(tier, tier, tier, tier, tier, tier, tier);
    }

    @Override
    public SpellLauncher getLauncher() {
        return new EarthTowerLauncher(towerSchematic);
    }
}
