package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

public class PacketLongIO implements PacketDataIO<Long> {
    @Override
    public Long read(ByteBuf buffer) {
        return buffer.readLong();
    }

    @Override
    public byte[] write(Long aLong) {
        byte b1 = (byte) ((aLong >> 56) & 0xFF);
        byte b2 = (byte) ((aLong >> 48) & 0xFF);
        byte b3 = (byte) ((aLong >> 40) & 0xFF);
        byte b4 = (byte) ((aLong >> 32) & 0xFF);
        byte b5 = (byte) ((aLong >> 24) & 0xFF);
        byte b6 = (byte) ((aLong >> 16) & 0xFF);
        byte b7 = (byte) ((aLong >> 8) & 0xFF);
        byte b8 = (byte) (aLong & 0xFF);
        System.out.println(aLong + " , " + b1 + "," +b2+"," +b3+"," +b4+"," +b5+"," +b6+"," +b7+"," +b8);
        return new byte[] {b1, b2, b3, b4, b5, b6, b7, b8};
    }
}
