package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

public class PacketStatus01JustAPing extends PacketData{
    @Getter
    @Setter
    private Long data;

    public PacketStatus01JustAPing() {
        super(1, "PING_PONG",PacketState.STATUS,  PacketType.BOTH);
    }
    public PacketStatus01JustAPing(Long data) {
        super(1, "PING_PONG",PacketState.STATUS,  PacketType.BOTH);
        this.data = data;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        data = packetIO.getDataReader(Long.class).read(buffer);
    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(Long.class).write(data);
    }
}
