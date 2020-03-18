package kr.minecheat.mcauth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;
import kr.minecheat.mcauth.packets.PacketHeader;

public class MinecraftPacketEncoder extends MessageToByteEncoder<PacketHeader> {
    private static final PacketDataWriter<VarInt> varIntWriter = Server.getDataIOProvider().getDataWriter(VarInt.class);

    private MinecraftPacketHandler handler;
    public MinecraftPacketEncoder(MinecraftPacketHandler pHandler) {
        this.handler = pHandler;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PacketHeader packetHeader, ByteBuf buffer) throws Exception {

        byte[] packetId = varIntWriter.write(new VarInt(packetHeader.getPacketId()));
        byte[] packetData = packetHeader.getData().writePacket();
        packetHeader.setLength(packetId.length + packetData.length);
        byte[] packetLen = varIntWriter.write(new VarInt(packetHeader.getLength()));

        buffer.writeBytes(packetLen);
        buffer.writeBytes(packetId);
        buffer.writeBytes(packetData);
    }
}
