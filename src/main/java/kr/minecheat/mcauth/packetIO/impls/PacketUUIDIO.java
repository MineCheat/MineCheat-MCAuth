package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PacketUUIDIO implements PacketDataIO<UUID> {

    @Override
    public UUID read(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    @Override
    public byte[] write(UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
