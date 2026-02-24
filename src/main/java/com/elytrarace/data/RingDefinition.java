package com.elytrarace.data;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Defines a race ring with type, orientation, bounds, and sequence order.
 * Supports both legacy point-based rings and WorldEdit region-based rings.
 */
public class RingDefinition {

    public enum RingType {
        POINT,   // Legacy: center + radius sphere detection
        REGION   // New: cuboid min/max bounds detection
    }

    public enum Orientation {
        VERTICAL_NS,  // Ring face in XY plane — player flies North/South through it
        VERTICAL_EW,  // Ring face in ZY plane — player flies East/West through it
        HORIZONTAL     // Ring face in XZ plane — player flies Up/Down through it
    }

    private final String name;
    private final int order;
    private final RingType type;
    private final Orientation orientation;
    private final double radius;
    private final World world;

    // POINT type: center location
    private final Location center;

    // REGION type: cuboid bounds (null for POINT)
    private final Location minCorner;
    private final Location maxCorner;

    /** Constructor for POINT type rings */
    public RingDefinition(String name, int order, Orientation orientation, double radius,
                          Location center) {
        this.name = name;
        this.order = order;
        this.type = RingType.POINT;
        this.orientation = orientation;
        this.radius = radius;
        this.world = center.getWorld();
        this.center = center;
        this.minCorner = null;
        this.maxCorner = null;
    }

    /** Constructor for REGION type rings */
    public RingDefinition(String name, int order, Orientation orientation, double radius,
                          Location minCorner, Location maxCorner) {
        this.name = name;
        this.order = order;
        this.type = RingType.REGION;
        this.orientation = orientation;
        this.radius = radius;
        this.world = minCorner.getWorld();
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
        // Compute center as midpoint of bounds
        this.center = new Location(world,
            (minCorner.getX() + maxCorner.getX()) / 2.0,
            (minCorner.getY() + maxCorner.getY()) / 2.0,
            (minCorner.getZ() + maxCorner.getZ()) / 2.0);
    }

    /**
     * Check if a player location is inside this ring's detection zone.
     */
    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(world)) return false;

        return switch (type) {
            case POINT -> loc.distanceSquared(center) <= radius * radius;
            case REGION -> loc.getX() >= minCorner.getX() && loc.getX() <= maxCorner.getX()
                        && loc.getY() >= minCorner.getY() && loc.getY() <= maxCorner.getY()
                        && loc.getZ() >= minCorner.getZ() && loc.getZ() <= maxCorner.getZ();
        };
    }

    /**
     * Get the visual radius for particle rendering.
     * For REGION type, uses half the smallest cross-section dimension.
     */
    public double getVisualRadius() {
        if (type == RingType.POINT) return radius;

        // For REGION, compute based on orientation
        double dx = maxCorner.getX() - minCorner.getX();
        double dy = maxCorner.getY() - minCorner.getY();
        double dz = maxCorner.getZ() - minCorner.getZ();

        return switch (orientation) {
            case VERTICAL_NS -> Math.max(dx, dy) / 2.0; // XY face
            case VERTICAL_EW -> Math.max(dz, dy) / 2.0; // ZY face
            case HORIZONTAL -> Math.max(dx, dz) / 2.0;  // XZ face
        };
    }

    // Getters
    public String getName() { return name; }
    public int getOrder() { return order; }
    public RingType getType() { return type; }
    public Orientation getOrientation() { return orientation; }
    public double getRadius() { return radius; }
    public World getWorld() { return world; }
    public Location getCenter() { return center; }
    public Location getMinCorner() { return minCorner; }
    public Location getMaxCorner() { return maxCorner; }
}
