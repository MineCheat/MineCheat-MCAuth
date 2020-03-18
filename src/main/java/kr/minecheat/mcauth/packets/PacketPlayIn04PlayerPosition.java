package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

public class PacketPlayIn04PlayerPosition extends PacketData {
    @Getter
    double x;
    @Getter
    double y;
    @Getter
    double z;
    @Getter
    boolean onGround;

    PacketPlayIn04PlayerPosition() {
        super(4, "PLAYER_POSITION", PacketState.PLAY, PacketType.SERVERBOUND);
    }


    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        x = packetIO.getDataReader(Double.class).read(buffer);
        y = packetIO.getDataReader(Double.class).read(buffer);
        z = packetIO.getDataReader(Double.class).read(buffer);
        onGround = packetIO.getDataReader(Boolean.class).read(buffer);
    }

    @Override
    public byte[] writePacket() throws Exception {
        return new byte[0];
    }
}
