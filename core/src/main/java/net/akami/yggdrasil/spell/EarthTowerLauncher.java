package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3f;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.schematic.Schematic;

public class EarthTowerLauncher implements SpellLauncher {

    private Schematic towerSchematic;

    public EarthTowerLauncher(Schematic towerSchematic) {
        this.towerSchematic = towerSchematic;
    }

    @Override
    public void commonLaunch(SpellCreationData data, Player caster) {
        Vector3f position = data.getProperty("position", Vector3f.class);
        long maxTime = data.getProperty("maxTime", Long.class);
        if(position == null) position = caster.getPosition().toFloat();
        Location<World> location = new Location<>(caster.getWorld(), position.toInt());

    }
}
