package kr.minecheat.mcauth.mcdata;

import lombok.Data;

@Data
public class VarLong {
    private Long value;

    public VarLong(Long value) {
        this.value = value;
    }
}
