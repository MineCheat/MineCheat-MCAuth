package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketShortIO implements PacketDataIO<Short> {

    @Override
    public Short read(ByteBuf buffer) {
        return buffer.readShort();
    }

    @Override
    public byte[] write(Short aShort) {
        return new byte[] {(byte) ((aShort >> 8) & 0xFF), (byte) (aShort & 0xFF)};
    }
}
