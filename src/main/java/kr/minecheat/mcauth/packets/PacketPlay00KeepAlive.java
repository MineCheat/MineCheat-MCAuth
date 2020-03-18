package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;
import lombok.Getter;
import lombok.Setter;

public class PacketPlay00KeepAlive extends PacketData {

    @Getter
    @Setter
    private int randomId;

    PacketPlay00KeepAlive() {
        super(0, "KEEP_ALIVE", PacketState.PLAY, PacketType.BOTH);
    }

    public PacketPlay00KeepAlive(int randomId) {
        super(0, "KEEP_ALIVE", PacketState.PLAY, PacketType.BOTH);
        this.randomId = randomId;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        randomId = packetIO.getDataReader(VarInt.class).read(buffer).getValue();
    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(VarInt.class).write(new VarInt(randomId));
    }
}
