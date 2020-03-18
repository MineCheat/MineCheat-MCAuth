package kr.minecheat.mcauth.mcdata;

import lombok.Getter;

public enum Difficulty {
    PEACEFUL(0), EASY(1), NORMAL(2), HARD(3);

    @Getter
    private byte id;
    private Difficulty(int i) {
        this.id = (byte) i;
    }
}
