package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;
import java.util.UUID;

public class PacketLogin02LoginSuccess extends PacketData {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private UUID uid = null;

    public PacketLogin02LoginSuccess() {
        super(2, "LOGIN_SUCCESS", PacketState.LOGIN, PacketType.CLIENTBOUND);
    }

    public PacketLogin02LoginSuccess(String username, UUID uid) {
        this();
        this.username= username;
        this.uid = uid;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] uid = packetIO.getDataWriter(String.class).write(this.uid.toString());
        byte[] uname = packetIO.getDataWriter(String.class).write(username);

        ByteBuffer bb = ByteBuffer.allocate(uid.length + uname.length);
        bb.put(uid);
        bb.put(uname);

        return bb.array();
    }
}
