package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;
import kr.minecheat.mcauth.packetIO.PacketDataReader;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;

public class PacketBooleanIO implements PacketDataIO<Boolean> {
    private static final byte[][] BYTES = {{0},{1}};

    @Override
    public Boolean read(ByteBuf buffer) {
        return buffer.readBoolean();
    }

    @Override
    public byte[] write(Boolean aBoolean) {
        return BYTES[aBoolean ? 1 : 0];
    }
}
