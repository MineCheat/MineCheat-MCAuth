package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

public class PacketPlayIn01ChatMessage extends PacketData {

    @Getter
    private String chat;

    PacketPlayIn01ChatMessage() {
        super(0x01, "CLIENT_CHAT", PacketState.PLAY, PacketType.SERVERBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        chat = packetIO.getDataReader(String.class).read(buffer);
    }

    @Override
    public byte[] writePacket() throws Exception {
        return new byte[0];
    }
}
