package kr.minecheat.mcauth.packetIO;

public abstract class PacketDataIOProvider {
    public abstract <T> PacketDataReader<T> getDataReader(Class<T> t);

    public abstract <T> PacketDataWriter<T> getDataWriter(Class<T> t);
}