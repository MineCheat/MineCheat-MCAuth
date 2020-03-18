package kr.minecheat.mcauth.mcdata;

import lombok.Data;

@Data
public class Location {
    private int x;
    private int y;
    private int z;

    public Location(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
