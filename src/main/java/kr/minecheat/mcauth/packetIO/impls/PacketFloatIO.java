package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketFloatIO implements PacketDataIO<Float> {

    @Override
    public Float read(ByteBuf buffer) {
        return buffer.readFloat();
    }

    @Override
    public byte[] write(Float aFloat) {
        int intBits = Float.floatToIntBits(aFloat);
        return Server.getDataIOProvider().getDataWriter(Integer.class).write(intBits);
    }
}
