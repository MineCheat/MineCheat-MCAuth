package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.UnsignedShort;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.packetIO.impls.PacketStringIO;
import kr.minecheat.mcauth.packetIO.impls.PacketVarIntIO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

public class PacketStatus00ServerListPing extends PacketData {
    @Getter
    @Setter
    int protocolVersion;
    @Getter
    @Setter
    String serverURL;
    @Getter
    @Setter
    int port;
    @Getter
    @Setter
    int nextState;

    @Getter
    boolean isRequestPacket;

    public PacketStatus00ServerListPing() {
        super(0, "ServerListPing",PacketState.STATUS,  PacketType.SERVERBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) {
        if (buffer.readableBytes() == 0) {
            isRequestPacket = true;
            return;
        }
        protocolVersion = packetIO.getDataReader(VarInt.class).read(buffer).getValue();
        serverURL = packetIO.getDataReader(String.class).read(buffer);
        port = packetIO.getDataReader(UnsignedShort.class).read(buffer).getValue();
        nextState = packetIO.getDataReader(VarInt.class).read(buffer).getValue();
    }

    @Override
    public byte[] writePacket() {
        return new byte[0];
    }
}
