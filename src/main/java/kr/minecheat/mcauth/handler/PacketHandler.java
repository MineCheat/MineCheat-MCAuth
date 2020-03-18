package kr.minecheat.mcauth.handler;

import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.mcdata.VarInt;
import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.packetIO.PacketDataWriter;
import kr.minecheat.mcauth.packets.PacketData;
import kr.minecheat.mcauth.packets.PacketHeader;
import kr.minecheat.mcauth.packets.PacketType;

public abstract class PacketHandler {
    MinecraftPacketHandler nettyHandler;

    public PacketHandler(MinecraftPacketHandler nettyHandler) {
        this.nettyHandler = nettyHandler;
    }

    public abstract void handlePacket(PacketHeader packetHeader) throws Exception;

    public void sendPacket(PacketData packetData) throws Exception {
        if (packetData.getPacketType() == PacketType.SERVERBOUND) throw new IllegalArgumentException("client bound packet only pls");

        PacketHeader pHeader = new PacketHeader();
        pHeader.setPacketId(packetData.getPacketID());
        pHeader.setData(packetData);

        nettyHandler.sendPacket(pHeader);
    }
}
