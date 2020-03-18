package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class PacketPlayOut08PlayerPositionAndLook extends PacketData {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private byte bitField;

    PacketPlayOut08PlayerPositionAndLook() {
        super(0x08, "PLAYER_POSITION_AND_LOOK", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut08PlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, byte bitField) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.bitField = bitField;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] f1 = packetIO.getDataWriter(Double.class).write(x);
        byte[] f2 = packetIO.getDataWriter(Double.class).write(y);
        byte[] f3 = packetIO.getDataWriter(Double.class).write(z);
        byte[] f4 = packetIO.getDataWriter(Float.class).write(yaw);
        byte[] f5 = packetIO.getDataWriter(Float.class).write(pitch);
        byte[] f6 = packetIO.getDataWriter(Byte.class).write(bitField);
        ByteBuffer buf = ByteBuffer.allocate(f1.length + f2.length + f3.length + f4.length + f5.length + f6.length);
        buf.put(f1);
        buf.put(f2);
        buf.put(f3);
        buf.put(f4);
        buf.put(f5);
        buf.put(f6);

        return buf.array();
    }
}
