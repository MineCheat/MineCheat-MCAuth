package kr.minecheat.mcauth.mcdata;

import lombok.Data;

import java.util.List;

@Data
public class MojangUser {
    private String id;
    private String name;
    private List<MojangUserProperties> properties;

    @Data
    public static class MojangUserProperties {
        private String name;
        private String value;
        private String signature;
    }
}
