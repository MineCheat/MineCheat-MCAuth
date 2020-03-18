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

        System.out.println("sent: " + packetHeader.getPacketId() + " / w/ data : "+  bytesToHex(packetData) + " / w/ len: "+bytesToHex(packetLen));

        buffer.writeBytes(packetLen);
        buffer.writeBytes(packetId);
        buffer.writeBytes(packetData);
    }
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }
}
