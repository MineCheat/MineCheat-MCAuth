package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

import java.nio.ByteBuffer;

public class PacketVarIntIO implements PacketDataIO<VarInt> {
    @Override
    public VarInt read(ByteBuf buffer) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buffer.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return new VarInt(result);
    }

    @Override
    public byte[] write(VarInt inte) {
        int integer = inte.getValue();
        ByteBuffer buf = ByteBuffer.allocate(5);
        do {
            byte temp = (byte)(integer & 0b01111111);
            integer >>>= 7;
            if (integer != 0) {
                temp |= 0b10000000;
            }
            buf.put(temp);
        } while (integer != 0);
        buf.flip();
        byte[] arr = new byte[buf.remaining()];
        buf.get(arr);
        return arr;
    }
}
