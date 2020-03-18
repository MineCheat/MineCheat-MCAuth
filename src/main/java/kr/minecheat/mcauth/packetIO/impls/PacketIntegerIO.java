package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketIntegerIO implements PacketDataIO<Integer> {

    @Override
    public Integer read(ByteBuf buffer) {
        return buffer.readInt();
    }

    @Override
    public byte[] write(Integer integer) {
        byte b1 = (byte) ((integer >> 6) & 0xFF);
        byte b2 = (byte) ((integer >> 4) & 0xFF);
        byte b3 = (byte) ((integer >> 2) & 0xFF);
        byte b4 = (byte) (integer & 0xFF);
        return new byte[] {b1, b2, b3, b4};
    }
}
