package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class PacketPlayOut3FPluginChannel extends PacketData {

    private String channel;
    private String data;

    PacketPlayOut3FPluginChannel() {
        super(0x3F, "PLUGIN_CHANNEL", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut3FPluginChannel(String channel, String data) {
        this();
        this.channel = channel;
        this.data = data;
    }


    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] f1 = packetIO.getDataWriter(String.class).write(channel);
        byte[] f2 = packetIO.getDataWriter(String.class).write(data);
        ByteBuffer bb = ByteBuffer.allocate(f1.length + f2.length);
        bb.put(f1);
        bb.put(f2);
        return bb.array();
    }
}
