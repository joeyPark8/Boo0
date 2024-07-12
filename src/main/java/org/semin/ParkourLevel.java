package org.semin;

import com.sk89q.worldedit.regions.Region;

public class ParkourLevel {
    Region region;

    String name;


    ParkourLevel(String name, Region region) {
        this.name = name;
        this.region = region;
    }
}
