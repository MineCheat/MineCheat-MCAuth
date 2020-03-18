package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.packetIO.PacketDataIOProvider;

public abstract class PacketData {
    private int packetId;
    private PacketType packetType;
    private PacketState packetState;
    private String packetName;

    static PacketDataIOProvider packetIO = Server.getDataIOProvider();

    PacketData(int packetId, String packetName, PacketState packetState,  PacketType type) {
        this.packetId = packetId;
        this.packetName = packetName;
        this.packetType = type;
        this.packetState = packetState;
    }

    public int getPacketID() {return packetId;}
    public String getPacketName() {return packetName;}
    public PacketType getPacketType() {return packetType;}
    public PacketState getPacketState() {return packetState;}

    public abstract void readPacket(ByteBuf buffer, PacketHeader header) throws Exception;
    public abstract byte[] writePacket() throws Exception;
}
