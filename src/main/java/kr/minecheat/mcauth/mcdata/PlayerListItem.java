package kr.minecheat.mcauth.mcdata;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PlayerListItem {

    private Action action;

    private List<? extends ActionValue> actions;

    private PlayerListItem(Builder builder) {
        action = builder.action;
        actions = builder.actions;
    }

    public static enum Action {
        ADD(0), UPDATE_GAMEMODE(1), UPDATE_LATENCY(2), UPDATE_DISPLAYNAME(3), REMOVE(4);

        @Getter
        private int id;
        private Action(int i) {
            this.id = i;
        }
    }

    public static class Builder {
        private Action action;
        private List<ActionValue> actions= new ArrayList<>();

        public Builder setAction(Action action) {
            this.action = action;
            return this;
        }

        public Builder addActionValue(ActionValue value) {
            if (value.getAction() != action) throw new IllegalStateException("action not equal");
            actions.add(value);
            return this;
        }

        public PlayerListItem build() {
            return new PlayerListItem(this);
        }
    }

    @Data
    public abstract static class ActionValue {
        private UUID uuid;

        public abstract Action getAction();

        ActionValue(UUID uuid) {this.uuid = uuid;}

        @Data
        public static class AddPlayer extends ActionValue {
            private String name;
            private List<MojangUser.MojangUserProperties> properties;
            private GameMode gameMode;
            private int ping;
            private String displayName;

            public AddPlayer(UUID uuid, String name, List<MojangUser.MojangUserProperties> properties, GameMode gameMode, int ping, String displayName) {
                super(uuid);
                this.name = name;
                this.properties = properties;
                this.gameMode = gameMode;
                this.ping = ping;
                this.displayName = displayName;
            }

            @Override
            public Action getAction() {
                return Action.ADD;
            }
        }

        @Data
        public static class UpdateGamemode extends ActionValue {
            private GameMode gameMode;

            public UpdateGamemode(UUID uuid, GameMode gameMode) {
                super(uuid);
                this.gameMode = gameMode;
            }

            @Override
            public Action getAction() {
                return Action.UPDATE_GAMEMODE;
            }
        }

        @Data
        public static class UpdatePing extends ActionValue {
            private int ping;

            public UpdatePing(UUID uuid, int ping) {
                super(uuid);
                this.ping = ping;
            }

            @Override
            public Action getAction() {
                return Action.UPDATE_LATENCY;
            }
        }

        @Data
        public static class UpdateDisplayName extends ActionValue {
            private String displayName;

            public UpdateDisplayName(UUID uuid, String displayName) {
                super(uuid);
                this.displayName = displayName;
            }

            @Override
            public Action getAction() {
                return Action.UPDATE_DISPLAYNAME;
            }
        }

        @Data
        public static class RemovePlayer extends ActionValue {
            public RemovePlayer(UUID uuid) {
                super(uuid);
            }

            @Override
            public Action getAction() {
                return Action.REMOVE;
            }
        }
    }

}
