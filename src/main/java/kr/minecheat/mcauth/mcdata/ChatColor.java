package kr.minecheat.mcauth.mcdata;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.Getter;

public enum ChatColor {
    DARK_BLACK("black", "0"),
    DARK_BLUE("dark_blue", "1"),
    DARK_GREEN("dark_green", "2"),
    DARK_CYAN("dark_aqua", "3"),
    DARK_RED("dark_red", "4"),
    DARK_PURPLE("dark_purple", "5"),
    LIGHT_GOLD("gold", "6"),
    LIGHT_GRAY("gray", "7"),
    DARK_GRAY("dark_gray", "8"),
    LIGHT_BLUE("blue", "9"),
    LIGHT_GREEN("green", "a"),
    LIGHT_CYAN("aqua", "b"),
    LIGHT_RED("red", "c"),
    LIGHT_PINK("light_purple", "d"),
    LIGHT_YELLOW("yellow", "e"),
    LIGHT_WHITE("white", "f");


    @Getter
    private String name;
    @Getter
    private String code;
    private ChatColor(String name, String code) {
        this.name = name;
        this.code = code;
    }
    @JsonValue
    public String toString() {
        return name;
    }
}
