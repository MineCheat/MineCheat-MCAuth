package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarLong;

import java.nio.ByteBuffer;
import java.util.logging.LoggingPermission;

public class PacketPlayOut03TimeUpdate extends PacketData {

    private long time;
    private long world;

    PacketPlayOut03TimeUpdate() {
        super(3, "TIME_UPDATE", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut03TimeUpdate(long time, long world) {
        this();
        this.time = time;
        this.world = world;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] f1 = packetIO.getDataWriter(Long.class).write((time));
        byte[] f2 = packetIO.getDataWriter(Long.class).write((world));
        ByteBuffer buf = ByteBuffer.allocate(f1.length + f2.length);
        buf.put(f1);
        buf.put(f2);
        return buf.array();
    }
}
