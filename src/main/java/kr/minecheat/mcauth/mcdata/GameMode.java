package kr.minecheat.mcauth.mcdata;

import lombok.Getter;

public enum GameMode {
    SURVIVAL(0), CREATIVE(1), ADVENTURE(2), SPECTATOR(3);

    @Getter
    byte id;
    private GameMode(int id) {
        this.id = (byte) id;
    }
}
