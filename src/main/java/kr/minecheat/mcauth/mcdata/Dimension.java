package kr.minecheat.mcauth.mcdata;

import lombok.Getter;

public enum Dimension {
    NETHER(-1), OVERWORLD(0), ENDER(1);

    @Getter
    private byte id;

    private Dimension(int id) {
        this.id = (byte) id;
    }
}
