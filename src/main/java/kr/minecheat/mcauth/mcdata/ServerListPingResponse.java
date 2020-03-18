package kr.minecheat.mcauth.mcdata;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ServerListPingResponse {
    private ModInfo modinfo;

    private VersionData version;

    private PlayerListData players;

    private Chat description;

    private String favicon;

    @Data
    public static class VersionData {
        private String name;
        private int protocol;

        public VersionData(String name, int protocol) {
            this.name = name;
            this.protocol = protocol;
        }
    }

    @Data
    public static class ModInfo {
        private List<String> modList;
        private String type;

        public ModInfo(List<String> modList, String type) {
            this.modList = modList;
            this.type = type;
        }
    }

    @Data
    public static class PlayerListData {
        private int max;
        private int online;
        private List<PlayerData> sample;

        private PlayerListData(Builder b) {
            max = b.max;
            online = b.online;
            sample = b.sample;
        }

        @Data
        public static class PlayerData {
            private String name;
            private UUID id;

            public PlayerData(String name, UUID id) {
                this.name = name;
                this.id = id;
            }
        }

        public static class Builder {
            private int max;
            private int online;
            private List<PlayerData> sample = new ArrayList<>();

            public Builder setMax(int max) {
                this.max = max;
                return this;
            }

            public Builder setOnline(int online) {
                this.online = online;
                return this;
            }

            public Builder setSample(List<PlayerData> sample) {
                this.sample = sample;
                return this;
            }

            public Builder addSample(PlayerData sample) {
                this.sample.add(sample);
                return this;
            }

            public PlayerListData build() {
                return new PlayerListData(this);
            }
        }
    }

}
