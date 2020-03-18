package kr.minecheat.mcauth.mcdata;

import lombok.Data;

@Data
public class VarInt {
    private Integer value;

    public VarInt(Integer value) {
        this.value = value;
    }
}
