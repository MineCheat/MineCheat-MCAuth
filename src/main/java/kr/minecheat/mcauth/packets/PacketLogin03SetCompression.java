package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;

public class PacketLogin03SetCompression extends PacketData {
    public PacketLogin03SetCompression() {
        super(3, "LOGIN_SET_COMPRESSION", PacketState.LOGIN, PacketType.CLIENTBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(VarInt.class).write(new VarInt(-1));
    }
}
