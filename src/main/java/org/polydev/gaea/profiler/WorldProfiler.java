package org.polydev.gaea.profiler;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.jafama.FastMath;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.polydev.gaea.generation.GaeaChunkGenerator;

import java.util.Map;

public class WorldProfiler {
    private final BiMap<String, Measurement> measures = HashBiMap.create();
    private final World world;
    private boolean isProfiling;

    public WorldProfiler(World w) {
        if(! (w.getGenerator() instanceof GaeaChunkGenerator))
            throw new IllegalArgumentException("Attempted to instantiate profiler on non-Gaea managed world!");
        this.addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "TotalChunkGenTime")
                .addMeasurement(new Measurement(2500000, DataType.PERIOD_MILLISECONDS), "ChunkBaseGenTime")
                .addMeasurement(new Measurement(2000000, DataType.PERIOD_MILLISECONDS), "BiomeApplyTime")
                .addMeasurement(new Measurement(2000000, DataType.PERIOD_MILLISECONDS), "PopulationManagerTime");
        isProfiling = false;
        this.world = w;
        ((GaeaChunkGenerator) w.getGenerator()).attachProfiler(this);
    }

    public String getResultsFormatted() {
        if(! isProfiling) return "Profiler is not currently running.";
        StringBuilder result = new StringBuilder(ChatColor.GOLD + "Gaea World Profiler Results (Min / Avg / Max / Std Dev): \n");
        for(Map.Entry<String, Measurement> e : measures.entrySet()) {
            result.append(ChatColor.GOLD)
                    .append(e.getKey())
                    .append(": ")
                    .append(e.getValue().getDataHolder().getFormattedData(e.getValue().getMin()))
                    .append(ChatColor.GOLD)
                    .append(" / ")
                    .append(e.getValue().getDataHolder().getFormattedData(e.getValue().average()))
                    .append(ChatColor.GOLD)
                    .append(" / ")
                    .append(e.getValue().getDataHolder().getFormattedData(e.getValue().getMax()))
                    .append(ChatColor.GOLD)
                    .append(" / ")
                    .append(ChatColor.GREEN)
                    .append((double) FastMath.round((e.getValue().getStdDev() / 1000000) * 100D) / 100D)
                    .append("ms")
                    .append(ChatColor.GOLD).append(" (x").append(e.getValue().size()).append(")\n");
        }
        return result.toString();
    }

    public void reset() {
        for(Map.Entry<String, Measurement> e : measures.entrySet()) {
            e.getValue().reset();
        }
    }

    public WorldProfiler addMeasurement(Measurement m, String name) {
        measures.put(name, m);
        return this;
    }

    public void setMeasurement(String id, long value) {
        if(isProfiling) measures.get(id).record(value);
    }

    public ProfileFuture measure(String id) {
        if(isProfiling) return measures.get(id).beginMeasurement();
        else return null;
    }

    public String getID(Measurement m) {
        return measures.inverse().get(m);
    }

    public boolean isProfiling() {
        return isProfiling;
    }

    public void setProfiling(boolean enabled) {
        this.isProfiling = enabled;
    }

    public World getWorld() {
        return world;
    }
}
