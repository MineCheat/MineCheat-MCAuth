package kr.minecheat.mcauth.mcdata;

import lombok.Data;

@Data
public class UnsignedByte {
    private Integer value;

    public UnsignedByte(Integer value) {
        if (value < 0) throw new IllegalArgumentException("unsigned byte has to be bigger than 0");
        if (value > 0xFF) throw new IllegalArgumentException("unsigned byte has to be smaller than 0xFF");
        this.value = value;
    }
}
