package net.gtn.dimensionalpocket.common.core.utils;

import me.jezza.oc.common.utils.CoordSet;
import net.minecraft.world.World;

/**
 * Wrapper of ForgeDirection for the teleporter.
 *
 * @author Jeremy
 *
 */
public enum TeleportDirection {

    //@formatter:off
    /** -Y  */
    DOWN(0, -2, 0),

    /** +Y  */
    UP(0, 1, 0),

    /** -Z  */
    NORTH(0, 0, -1), NORTH_MINUS_ONE(0, -1, -1),

    /** +Z  */
    SOUTH(0, 0, 1), SOUTH_MINUS_ONE(0, -1, 1),

    /** -X  */
    WEST(-1, 0, 0), WEST_MINUS_ONE(-1, -1, 0),

    /** +X  */
    EAST(1, 0, 0), EAST_MINUS_ONE(1, -1, 0),

    /** -Z -X */
    NORTHWEST(-1, 0, -1), NORTHWEST_MINUS_ONE(-1, -1, -1),

    /** -Z +X */
    NORTHEAST(1, 0, -1), NORTHEAST_MINUS_ONE(1, -1, -1),

    /** +Z -X */
    SOUTHWEST(-1, 0, 1), SOUTHWEST_MINUS_ONE(-1, -1, 1),

    /** +Z +X */
    SOUTHEAST(1, 0, 1), SOUTHEAST_MINUS_ONE(1, -1, 1),

    UNKNOWN(0, 0, 0);
    //@formatter:on

    public static final TeleportDirection[] VALID_DIRECTIONS = {
        UP, DOWN, NORTH, SOUTH, WEST, EAST,
        NORTH_MINUS_ONE, SOUTH_MINUS_ONE, WEST_MINUS_ONE, EAST_MINUS_ONE,
        NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST,
        NORTHWEST_MINUS_ONE, NORTHEAST_MINUS_ONE, SOUTHWEST_MINUS_ONE, SOUTHEAST_MINUS_ONE
    };

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;

    private TeleportDirection(int x, int y, int z) {
        offsetX = x;
        offsetY = y;
        offsetZ = z;
    }

    private static boolean isAir(World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z) && world.isAirBlock(x, y + 1, z);
    }

    public static TeleportDirection getValidTeleportLocation(World world, int x, int y, int z) {
        for (TeleportDirection direction : VALID_DIRECTIONS)
            if (isAir(world, x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ))
                return direction;

        return UNKNOWN;
    }

    public CoordSet toCoordSet() {
        return new CoordSet(offsetX, offsetY, offsetZ);
    }
}
