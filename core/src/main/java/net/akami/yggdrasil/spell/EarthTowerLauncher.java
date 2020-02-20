package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3f;
import net.akami.yggdrasil.api.data.YggdrasilKeys;
import net.akami.yggdrasil.api.spell.SpellCreationData;
import net.akami.yggdrasil.api.spell.SpellLauncher;
import net.akami.yggdrasil.api.utils.YggdrasilMath;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EarthTowerLauncher implements SpellLauncher<EarthTowerLauncher> {

    @Override
    public void commonLaunch(SpellCreationData<EarthTowerLauncher> data, Player caster) {
        Vector3f position = data.getPropertyMap().getPropertyOrElse("position", Vector3f.class, caster.getPosition().toFloat());
        AtomicLong maxTime = new AtomicLong(data.getPropertyMap().getProperty("time", Long.class));
        int maxSize = data.getPropertyMap().getPropertyOrElse("maxSize", Integer.class, 10);
        int radius = data.getPropertyMap().getProperty("radius", Integer.class);
        int speed = data.getPropertyMap().getProperty("speed", Integer.class);
        Location<World> location = new Location<>(caster.getWorld(), position.toInt());

        EarthTower tower = new EarthTower(caster, location, BLOCK_STATES, new Random(), maxSize, radius);
        Task.Builder towerTaskBuilder = Task.builder()
                .name("earthTower")
                .intervalTicks(1)
                .execute(task -> {
                    if(maxTime.getAndDecrement() < 0) {
                        task.cancel();
                        tower.destroy();
                        return;
                    }
                    if(caster.get(Keys.IS_SNEAKING).orElse(false)) tower.grow(speed);
                });

        Object plugin = Sponge.getPluginManager().getPlugin("yggdrasil").orElseThrow(IllegalStateException::new);

        if(data.isStorable()) {
            ItemStack grass = ItemStack.builder().itemType(ItemTypes.GRASS).add(Keys.DISPLAY_NAME, Text.of(UUID.randomUUID())).build();
            Item item = (Item) location.getExtent().createEntity(EntityTypes.ITEM, caster.getPosition().add(0, 1, 0));
            item.setVelocity(YggdrasilMath.headRotationToDirection(caster.getHeadRotation()).mul(1.2));
            item.offer(Keys.REPRESENTED_ITEM, grass.createSnapshot());
            item.offer(YggdrasilKeys.PERSISTENT, true);
            item.offer(Keys.PICKUP_DELAY, 999);
            item.offer(Keys.DESPAWN_DELAY, 999);
            location.getExtent().spawnEntity(item);

            Task.builder()
                    .name("earthProjectile")
                    .intervalTicks(1)
                    .execute(task -> {
                        if(caster.get(Keys.IS_SNEAKING).orElse(false)) {
                            tower.setLocation(item.getLocation());
                            towerTaskBuilder.submit(plugin);
                            item.remove();
                            task.cancel();
                        }
                    }).submit(plugin);
        } else {
            towerTaskBuilder.submit(plugin);
        }
    }

    private static final List<BlockState> BLOCK_STATES = Arrays.asList(
            BlockState.builder().blockType(BlockTypes.STAINED_HARDENED_CLAY).add(Keys.DYE_COLOR, DyeColors.BLACK).build(),
            BlockState.builder().blockType(BlockTypes.STAINED_HARDENED_CLAY).add(Keys.DYE_COLOR, DyeColors.BROWN).build(),
            BlockState.builder().blockType(BlockTypes.DIRT).build()
    );
}
