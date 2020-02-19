package net.akami.yggdrasil.api.utils;

import com.flowpowered.math.vector.Vector3d;
import net.akami.yggdrasil.api.life.LivingEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.AABB;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class YggdrasilMath {

    private YggdrasilMath() {}

    public static Vector3d headRotationToDirection(Vector3d headRot) {
        double pitch = Math.toRadians(-headRot.getX());
        double yaw = Math.toRadians(-headRot.getY());
        double x = Math.sin(yaw)*Math.cos(pitch);
        double z = Math.cos(yaw)*Math.cos(pitch);
        double y = Math.sin(pitch);
        return new Vector3d(x, y/1.5, z);
    }

    public static double velocityToFallingDistance(double yVelocity) {
        return Math.max(2.8 * Math.exp(1.298 * Math.abs(yVelocity)) - 5, 0);
    }

    public static BiFunction<Float, Integer, Float> instantCostFunction(Function<Integer, Float> costPerTier) {
        return (time, tier) -> time == 0 ? costPerTier.apply(tier) : 0f;
    }

    public static Function<Integer, Float> standardPolynomialFunction(float initial) {
        return (tier) -> initial + (float) Math.pow(2 * tier - 1, 1.3) - 1;
    }

    public static BiFunction<Float, Integer, Float> instantStandardPolynomialFunction(float initial) {
        return instantCostFunction(standardPolynomialFunction(initial));
    }

    public static Entity getTargetEntity(Location<World> location, Vector3d direction, double range, Predicate<Entity> filter) {
        TreeMap<Double, Entity> treeMap = new TreeMap<>(Double::compareTo);
        for(Entity entity : location.getExtent().getNearbyEntities(location.getPosition(), range)) {
            if(!filter.test(entity)) continue;
            if(!(entity instanceof LivingEntity)) continue;
            if(!entity.getBoundingBox().isPresent()) continue;
            AABB boundingBox = entity.getBoundingBox().get();
            Vector3d vectorLocation = location.getPosition();
            Optional<Tuple<Vector3d, Vector3d>> intersection = boundingBox.intersects(vectorLocation, direction);
            if(!intersection.isPresent()) continue;
            treeMap.put(intersection.get().getFirst().distance(vectorLocation), entity);
        }
        if(treeMap.isEmpty()) return null;
        return treeMap.lastEntry().getValue();
    }
}
