package kr.minecheat.mcauth.packetIO;

import io.netty.buffer.ByteBuf;

public interface PacketDataReader<T> {
    T read(ByteBuf buffer);
}
