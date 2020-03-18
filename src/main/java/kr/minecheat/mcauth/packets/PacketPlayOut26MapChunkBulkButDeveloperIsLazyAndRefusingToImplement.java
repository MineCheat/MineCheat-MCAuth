package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.mcdata.VarLong;

import java.nio.ByteBuffer;

public class PacketPlayOut26MapChunkBulkButDeveloperIsLazyAndRefusingToImplement extends PacketData {

    private static final byte[] wood_chunkbytes;
    static {
        byte[] f1 = {1, 1, 0,0,0,0,0,0,0,0,0,1};
        byte[] f3 = new byte[2 * 16 * 16 * 16]; // Filled with 0x50 0x00
        byte[] f4 = new byte[16*16*16]; // Filled With 1
        byte[] f5 = new byte[256]; // Filled With 1
        for (int i = 0; i < 4096; i++) {
            f3[i*2] = 0x50;
            f3[i*2 + 1] = 0;
            f4[i] = 1;
        }
        for (int i = 0; i < 256; i++)
            f5[i] = 1;
        ByteBuffer buf = ByteBuffer.allocate(f1.length + f3.length + f4.length + f5.length);
        buf.put(f1);
        buf.put(f3);
        buf.put(f4);
        buf.put(f5);
        wood_chunkbytes = buf.array();
    }

    public PacketPlayOut26MapChunkBulkButDeveloperIsLazyAndRefusingToImplement() {
        super(0x26, "MapChunkBulkButDeveloperIsLazyAndRefusingToImplement", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        return wood_chunkbytes;
    }
}
