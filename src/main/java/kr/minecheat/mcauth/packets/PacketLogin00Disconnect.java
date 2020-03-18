package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.Chat;
import lombok.Getter;
import lombok.Setter;

public class PacketLogin00Disconnect extends PacketData {

    @Getter
    @Setter
    private Chat disconnect;

    PacketLogin00Disconnect() {
        super(0, "DISCONNECT", PacketState.LOGIN, PacketType.CLIENTBOUND);
    }

    public PacketLogin00Disconnect(Chat disconnect) {
        super(0, "DISCONNECT", PacketState.LOGIN, PacketType.CLIENTBOUND);
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
