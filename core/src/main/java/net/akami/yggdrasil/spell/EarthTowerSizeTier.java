package net.akami.yggdrasil.spell;

import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellTier;
import org.spongepowered.api.entity.living.player.Player;

public class EarthTowerSizeTier implements SpellTier<EarthTowerLauncher> {

    private int maxSize;

    public EarthTowerSizeTier(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void definePreLaunchProperties(Player caster, SpellCreationData<EarthTowerLauncher> data) {
        data.setProperty("maxSize", maxSize);
    }
}
