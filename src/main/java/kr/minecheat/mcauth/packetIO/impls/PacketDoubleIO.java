package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketDoubleIO implements PacketDataIO<Double> {
    @Override
    public Double read(ByteBuf buffer) {
        return buffer.readDouble();
    }

    @Override
    public byte[] write(Double aDouble) {
        Long longBits = Double.doubleToLongBits(aDouble);
        return Server.getDataIOProvider().getDataWriter(Long.class).write(longBits);
    }
}
