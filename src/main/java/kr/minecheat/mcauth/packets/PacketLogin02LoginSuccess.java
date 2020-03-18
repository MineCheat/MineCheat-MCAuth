package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

public class PacketLogin02LoginSuccess extends PacketData {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String userId;

    public PacketLogin02LoginSuccess() {
        super(2, "LOGIN_SUCCESS", PacketState.LOGIN, PacketType.CLIENTBOUND);
    }

    public PacketLogin02LoginSuccess(String username, String userId) {
        super(2, "LOGIN_SUCCESS", PacketState.LOGIN, PacketType.CLIENTBOUND);
        this.username= username;
        this.userId = userId;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] uid = packetIO.getDataWriter(String.class).write(userId);
        byte[] uname = packetIO.getDataWriter(String.class).write(username);

        ByteBuffer bb = ByteBuffer.allocate(uid.length + uname.length);
        bb.put(uid);
        bb.put(uname);

        return bb.array();
    }
}
