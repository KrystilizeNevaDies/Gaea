package org.polydev.gaea.biome;

import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.world.palette.Palette;

public abstract class Generator {
    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    public abstract double getNoise(FastNoiseLite gen, World w, int x, int z);

    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param y   - The y coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    public abstract double getNoise(FastNoiseLite gen, World w, int x, int y, int z);

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    public abstract Palette<BlockData> getPalette(int y);

    /**
     * Returns true if the biome should be interpolated just once, false to use advanced interpolation + blending.
     * @return Whether biome should use minimal interpolation
     */
    public abstract boolean useMinimalInterpolation();


    /**
     * Get the type of interpolation to use in this biome.
     * @return Interpolation type
     */
    public Interpolator.Type getInterpolationType() {
        return Interpolator.Type.LINEAR;
    }
}
