package kr.minecheat.mcauth.exception;

public class MalformedFieldLengthException extends RuntimeException {
    public MalformedFieldLengthException(int overLen)  {
        super("malformed field length :: "+overLen);
    }
}
