package kr.minecheat.mcauth.mcdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class ChatClickEvent {
    private Action action;
    private String value;

    public ChatClickEvent(Action action, String value) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(value);
        this.action = action;
        this.value = value;
    }

    public static enum Action {
        OPEN_URL("open_url"), RUN_COMMAND("run_command"), REPLACE_CHATBOX("suggest_command"), CHANGE_PAGE("change_page");

        @Getter
        private String name;
        private Action(String name) {
            this.name = name;
        }

        @JsonValue
        public String toString() {
            return name;
        }
    }
}
