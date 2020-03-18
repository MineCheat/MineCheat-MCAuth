package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.UnsignedByte;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketUnsignedByteIO implements PacketDataIO<UnsignedByte> {
    @Override
    public UnsignedByte read(ByteBuf buffer) {
        return new UnsignedByte((int) buffer.readByte() & 0xFF);
    }
    @Override
    public byte[] write(UnsignedByte integer) {
        return new byte[] {(byte) (integer.getValue() & 0xFF)};
    }
}
