package kr.minecheat.mcauth.mcdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Data
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class ChatHoverEvent {
    private Action action;
    private Chat value;

    public ChatHoverEvent(Action action, Chat value) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(value);
        this.action = action;
        this.value = value;
    }

    public static enum Action {
        SHOW_TEXT("show_text"),
        SHOW_ITEM("show_item"),
        SHOW_ENTITY("show_entity");

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
