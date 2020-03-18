package kr.minecheat.mcauth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.packetIO.PacketDataReader;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;
import kr.minecheat.mcauth.packets.PacketData;
import kr.minecheat.mcauth.packets.PacketDataRegistry;
import kr.minecheat.mcauth.packets.PacketHeader;

import java.util.List;

public class MinecraftPacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final PacketDataReader<VarInt> varIntReader = Server.getDataIOProvider().getDataReader(VarInt.class);
    private static final PacketDataWriter<VarInt> varIntWriter = Server.getDataIOProvider().getDataWriter(VarInt.class);

    private MinecraftPacketHandler handler;

    public MinecraftPacketDecoder(MinecraftPacketHandler pHandler) {
        this.handler = pHandler;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        PacketHeader header = new PacketHeader();
        header.setPacketId(varIntReader.read(byteBuf).getValue());

        System.out.println("received : " + header.getPacketId() + " / " +handler.getCurrentState());
        PacketData pd;
        try {
            pd = PacketDataRegistry.getPacketData(header, handler.getCurrentState());
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return;
        }

        try {
            pd.readPacket(byteBuf, header);
            header.setData(pd);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        list.add(header);
    }
}
