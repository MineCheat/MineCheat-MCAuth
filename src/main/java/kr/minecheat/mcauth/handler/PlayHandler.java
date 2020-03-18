package kr.minecheat.mcauth.handler;

import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.packets.PacketHeader;

public class PlayHandler extends PacketHandler {
    public PlayHandler(MinecraftPacketHandler nettyHandler) {
        super(nettyHandler);
    }

    @Override
    public void handlePacket(PacketHeader packetHeader) throws Exception {

    }

    void initiate() {

    }
}
