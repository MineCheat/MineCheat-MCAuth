package kr.minecheat.mcauth.packetIO.impls;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.exception.MalformedFieldLengthException;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.packetIO.PacketDataIO;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketStringIO implements PacketDataIO<String> {
    @Override
    public String read(ByteBuf buffer) {
        int len = Server.getDataIOProvider().getDataReader(VarInt.class).read(buffer).getValue();
        if (len > 32767) throw new MalformedFieldLengthException(len);

        return buffer.readCharSequence(len, Charset.defaultCharset()).toString();
    }

    @Override
    public byte[] write(String s) {
        if (s.length() > 32767) throw new MalformedFieldLengthException(s.length());

        byte[] str = s.getBytes(StandardCharsets.UTF_8);
        byte[] len = Server.getDataIOProvider().getDataWriter(VarInt.class).write(new VarInt(str.length));
        ByteBuffer byteBuffer = ByteBuffer.allocate(len.length + str.length);
        byteBuffer.put(len);
        byteBuffer.put(str);

        return byteBuffer.array();
    }
}
