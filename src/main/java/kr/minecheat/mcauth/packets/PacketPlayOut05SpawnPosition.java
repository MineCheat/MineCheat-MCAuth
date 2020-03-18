package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.Location;

public class PacketPlayOut05SpawnPosition extends PacketData {

    private Location position;

    PacketPlayOut05SpawnPosition() {
        super(0x05, "SPAWN_POSITION", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut05SpawnPosition(Location loc) {
        this();
        this.position = loc;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(Location.class).write(position);
    }
}
