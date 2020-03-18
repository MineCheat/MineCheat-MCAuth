package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.UnsignedShort;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketUnsignedShortIO implements PacketDataIO<UnsignedShort> {
    @Override
    public UnsignedShort read(ByteBuf buffer) {
        return new UnsignedShort(buffer.readShort() & 0xFFFF);
    }

    @Override
    public byte[] write(UnsignedShort integerr) {
        int integer = integerr.getValue();
        return new byte[] {(byte) ((integer >> 8) & 0xFF), (byte) (integer & 0xFF)};
    }
}
