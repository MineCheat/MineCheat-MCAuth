package kr.minecheat.mcauth.packetIO;

public interface PacketDataWriter<T> {
    byte[] write(T t);
}
