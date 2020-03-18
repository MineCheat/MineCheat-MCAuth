package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketByteIO implements PacketDataIO<Byte> {
    @Override
    public Byte read(ByteBuf buffer) {
        return buffer.readByte();
    }

    @Override
    public byte[] write(Byte aByte) {
        return new byte[] {aByte};
    }
}
