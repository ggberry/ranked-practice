package me.gege.seed;

public class WorldInfo {
    public final long overworldSeed;
    public final int overworldChunkX;
    public final int overworldChunkZ;
    public final long netherSeed;
    public final int seedType;

    public WorldInfo(long overworldSeed, int overworldChunkX, int overworldChunkZ, long netherSeed, int seedType) {
        this.overworldSeed = overworldSeed;
        this.overworldChunkX = overworldChunkX;
        this.overworldChunkZ = overworldChunkZ;
        this.netherSeed = netherSeed;
        this.seedType = seedType;
    }
}
