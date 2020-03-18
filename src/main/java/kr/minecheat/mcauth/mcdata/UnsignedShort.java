package kr.minecheat.mcauth.mcdata;

import lombok.Data;

@Data
public class UnsignedShort {
    private Integer value;

    public UnsignedShort(Integer value) {
        if (value < 0) throw new IllegalArgumentException("unsigned short has to be bigger than 0");
        if (value > 0xFFFF) throw new IllegalArgumentException("unsigned short has to be smaller than 0xFFFF");
        this.value = value;
    }
}
