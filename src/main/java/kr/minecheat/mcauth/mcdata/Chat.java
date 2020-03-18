package kr.minecheat.mcauth.mcdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class Chat {
    public static class Builder {
        private String text;
        private boolean bold;
        private boolean italic;
        private boolean underlined;

        private boolean strikethrough;
        private boolean obfuscated;
        private ChatColor color;

        private String insertion;

        private ChatClickEvent clickEvent;
        private ChatHoverEvent hoverEvent;

        private List<Chat> extra;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setBold(boolean bold) {
            this.bold = bold;
            return this;
        }

        public Builder setItalic(boolean italic) {
            this.italic = italic;
            return this;
        }

        public Builder setUnderlined(boolean underlined) {
            this.underlined = underlined;
            return this;
        }

        public Builder setStrikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder setObfuscated(boolean obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }

        public Builder setColor(ChatColor color) {
            this.color = color;
            return this;
        }

        public Builder setInsertion(String insertion) {
            this.insertion = insertion;
            return this;
        }

        public Builder setClickEvent(ChatClickEvent clickEvent) {
            this.clickEvent = clickEvent;
            return this;
        }

        public Builder setHoverEvent(ChatHoverEvent hoverEvent) {
            this.hoverEvent = hoverEvent;
            return this;
        }

        public Builder setExtra(List<Chat> extra) {
            this.extra = extra;
            return this;
        }

        public Chat build() {
            return new Chat(this);
        }


    }

    private Chat(Builder b) {
        text = b.text;
        bold = b.bold;
        italic = b.italic;
        underlined = b.underlined;
        strikethrough = b.strikethrough;
        obfuscated = b.obfuscated;
        color = b.color;
        insertion = b.insertion;
        clickEvent = b.clickEvent;
        hoverEvent = b.hoverEvent;
        extra = b.extra;
    }

    private String text;
    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean obfuscated;
    private ChatColor color;

    private String insertion;

    private ChatClickEvent clickEvent;
    private ChatHoverEvent hoverEvent;

    private List<Chat> extra;
}
