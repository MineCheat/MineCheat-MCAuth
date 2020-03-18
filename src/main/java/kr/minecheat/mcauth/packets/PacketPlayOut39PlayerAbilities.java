package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class PacketPlayOut39PlayerAbilities extends PacketData {

    private byte flags;
    private float flying_speed;
    private float field_of_view_modifier;

    PacketPlayOut39PlayerAbilities() {
        super(0x39, "PLAYER_ABILITIES", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut39PlayerAbilities(Byte flags, float flying_speed, float field_of_view_modifier) {
        this();
        this.flags = flags;
        this.flying_speed = flying_speed;
        this.field_of_view_modifier = field_of_view_modifier;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] bytes1 = packetIO.getDataWriter(Byte.class).write(flags);
        byte[] bytes2 = packetIO.getDataWriter(Float.class).write(flying_speed);
        byte[] bytes3 = packetIO.getDataWriter(Float.class).write(field_of_view_modifier);

        ByteBuffer bb = ByteBuffer.allocate(bytes1.length + bytes2.length + bytes3.length);
        bb.put(bytes1);
        bb.put(bytes2);
        bb.put(bytes3);

        return bb.array();
    }
}
