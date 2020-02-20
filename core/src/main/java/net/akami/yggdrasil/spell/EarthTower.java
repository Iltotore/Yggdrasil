package net.akami.yggdrasil.spell;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class EarthTower {

    private Player owner;
    private Location<World> location;
    private List<BlockInfo> infos = new ArrayList<>();
    private List<BlockState> possibleStates;
    private Random random;
    private int size = 0;
    private int maxSize, radius;

    public EarthTower(Player owner, Location<World> location, Collection<BlockState> possibleStates, Random random, int maxSize, int radius) {
        this.owner = owner;
        this.location = location;
        this.possibleStates = new ArrayList<>(possibleStates);
        this.random = random;
        this.maxSize = maxSize;
        this.radius = radius;
    }

    public void grow(int growSize) {
        for(int y = size; y < Math.min(size + growSize, maxSize); y++) {
            for(int x = -radius; x < radius; x++) {
                for(int z = -radius; z < radius; z++) {
                    Location<World> newLocation = location.copy().add(x, y, z);
                    BlockState newState = possibleStates.get(random.nextInt(possibleStates.size()));
                    infos.add(new BlockInfo(newLocation.getBlock(), newState, newLocation.getBlockPosition()));
                    newLocation.setBlock(newState);
                    newLocation.getExtent().playSound(newState.getType().getSoundGroup().getPlaceSound(), newLocation.getPosition(), 1);
                    if(owner.getPosition().getFloorX() == newLocation.getBlockX() && owner.getPosition().getFloorY() == newLocation.getBlockY() && owner.getPosition().getFloorZ() == newLocation.getBlockZ()) {
                        owner.setLocation(newLocation.add(0, 1, 0));
                    }
                }
            }
        }
        size = Math.min(size + growSize, maxSize);
    }

    public void destroy() {
        for(BlockInfo info : infos) {
            Location<World> newLocation = new Location<>(location.getExtent(), info.getPosition());
            if(newLocation.getBlock().equals(info.getNewState())) newLocation.setBlock(info.getOldState());
        }
    }

    public void setLocation(Location<World> location) {
        this.location = location;
    }

    public static class BlockInfo {

        private BlockState oldState;
        private BlockState newState;
        private Vector3i position;

        public BlockInfo(BlockState oldState, BlockState newState, Vector3i position) {
            this.oldState = oldState;
            this.newState = newState;
            this.position = position;
        }

        public BlockState getOldState() {
            return oldState;
        }

        public BlockState getNewState() {
            return newState;
        }

        public Vector3i getPosition() {
            return position;
        }
    }
}
