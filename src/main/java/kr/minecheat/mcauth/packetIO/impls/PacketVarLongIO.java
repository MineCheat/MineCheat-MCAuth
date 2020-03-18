package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarLong;
import kr.minecheat.mcauth.packetIO.PacketDataIO;

import java.nio.ByteBuffer;

public class PacketVarLongIO implements PacketDataIO<VarLong> {
    @Override
    public VarLong read(ByteBuf buffer) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buffer.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return new VarLong(result);
    }

    @Override
    public byte[] write(VarLong integerr) {
        long integer = integerr.getValue();
        ByteBuffer buf = ByteBuffer.allocate(10);
        do {
            byte temp = (byte)(integer & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
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
