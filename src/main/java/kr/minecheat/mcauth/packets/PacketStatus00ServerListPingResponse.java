package kr.minecheat.mcauth.packets;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.ServerListPingResponse;
import lombok.Getter;
import lombok.Setter;

public class PacketStatus00ServerListPingResponse extends PacketData {

    @Getter
    @Setter
    private ServerListPingResponse response;

    public PacketStatus00ServerListPingResponse() {
        super(0, "ServerListPingResponse",PacketState.STATUS,  PacketType.CLIENTBOUND);
    }

    public PacketStatus00ServerListPingResponse(ServerListPingResponse response) {
        super(0, "ServerListPingResponse",PacketState.STATUS,  PacketType.CLIENTBOUND);
        this.response = response;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception{

    }

    @Override
    public byte[] writePacket() throws Exception {
        String str = Server.getMapper().writeValueAsString(response);
        byte[] bytes = packetIO.getDataWriter(String.class).write(str);
        return bytes;
    }
}
