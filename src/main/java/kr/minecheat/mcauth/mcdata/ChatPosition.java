package kr.minecheat.mcauth.mcdata;

import lombok.Getter;

public enum ChatPosition {
    CHAT(0), SYSTEM_MESSAGE(1), HOTBAR(2);

    @Getter
    private byte id;
    private ChatPosition(int id) {
        this.id = (byte) id;
    }
}
