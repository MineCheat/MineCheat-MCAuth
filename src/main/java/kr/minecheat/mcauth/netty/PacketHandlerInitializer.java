package kr.minecheat.mcauth.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class PacketHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    public void initChannel(Channel channel) {
        ChannelPipeline p = channel.pipeline();

        MinecraftPacketHandler pHandler = new MinecraftPacketHandler();

        p.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
        p.addLast("packetDecoder", new MinecraftPacketDecoder(pHandler));
        p.addLast("packetEncoder", new MinecraftPacketEncoder(pHandler));
        p.addLast("packetHandler", pHandler);
    }
}
