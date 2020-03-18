package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.Chat;
import lombok.Getter;
import lombok.Setter;

public class PacketPlayOut40Disconnect extends PacketData {
    @Getter
    @Setter
    private Chat disconnect;

    PacketPlayOut40Disconnect() {
        super(0x40, "PLAY_DISCONNECT", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut40Disconnect(Chat disconnect) {
        this();
        this.disconnect = disconnect;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        String str = Server.getMapper().writeValueAsString(disconnect);
        System.out.println(str);
        byte[] bytes = packetIO.getDataWriter(String.class).write(str);
        return bytes;
    }
}
