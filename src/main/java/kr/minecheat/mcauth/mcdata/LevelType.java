package kr.minecheat.mcauth.mcdata;

import lombok.Getter;
import lombok.ToString;

public enum LevelType {
    DEFAULT("default"), FLAT("flat"), LARGEBIOMES("largeBiomes"), AMPLIFIED("amplified"), DEFAULT_1_1("default_1_1");

    @Getter
    private String PacketName;
    private LevelType(String name) {
        this.PacketName= name;
    }
}
