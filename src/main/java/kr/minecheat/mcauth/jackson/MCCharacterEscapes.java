package kr.minecheat.mcauth.jackson;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;

public class MCCharacterEscapes extends CharacterEscapes
{
    private final int[] asciiEscapes;

    public MCCharacterEscapes()
    {
        // start with set of characters known to require escaping (double-quote, backslash etc)
        int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
        // and force escaping of a few others:
        esc['/'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes = esc;
    }
    // this method gets called for character codes 0 - 127
    @Override public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }
    // and this for others; we don't need anything special here
    @Override public SerializableString getEscapeSequence(int ch) {
        if(ch == '/'){
            return new SerializedString("\\/");
        }
        else{
            return null;
        }
    }
}