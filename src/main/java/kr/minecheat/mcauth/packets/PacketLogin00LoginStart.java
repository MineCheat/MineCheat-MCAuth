package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

public class PacketLogin00LoginStart extends PacketData {

    @Getter
    @Setter
    private String username;


    PacketLogin00LoginStart() {
        super(0, "LOGIN_START", PacketState.LOGIN, PacketType.SERVERBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        username = packetIO.getDataReader(String.class).read(buffer);
    }

    @Override
    public byte[] writePacket() throws Exception {
        return new byte[0];
    }
}
