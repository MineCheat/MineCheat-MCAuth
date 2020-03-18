package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.Location;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketLocationIO implements PacketDataIO<Location> {
    @Override
    public Location read(ByteBuf buffer) {
        long rawValue = Server.getDataIOProvider().getDataReader(Long.class).read(buffer);
        int x = (int) (rawValue >> 38);
        int y = (int) ((rawValue >> 26) & 0xFFF);
        int z = (int) (rawValue << 38 >> 38);
        return new Location(x,y,z);
    }

    @Override
    public byte[] write(Location location) {
        int x = location.getX();
        int y = location.getY();
        int z = location.getZ();
        long rawValue = ((x & 0x3FFFFFF) << 38) | ((y & 0xFFF) << 26) | (z & 0x3FFFFFF);
        return Server.getDataIOProvider().getDataWriter(Long.class).write(rawValue);
    }
}
