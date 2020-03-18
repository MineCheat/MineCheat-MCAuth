package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.Chat;
import kr.minecheat.mcauth.mcdata.ChatPosition;

import java.nio.ByteBuffer;

public class PacketPlayOut02Chat extends PacketData {

    private Chat chat;
    private ChatPosition position;

    PacketPlayOut02Chat() {
        super(0x02, "CHAT", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut02Chat(Chat chat, ChatPosition position) {
        this();
        this.chat = chat;
        this.position = position;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        String str = Server.getMapper().writeValueAsString(chat);
        byte[] b1 = packetIO.getDataWriter(String.class).write(str);
        byte[] b2 = packetIO.getDataWriter(Byte.class).write(position.getId());
        ByteBuffer buffer = ByteBuffer.allocate(b1.length + b2.length);
        buffer.put(b1);
        buffer.put(b2);

        return buffer.array();
    }
}
