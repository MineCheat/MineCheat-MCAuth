package kr.minecheat.mcauth.packets;

import lombok.Data;

@Data
public class PacketHeader {
    private int packetId;
    private int length;
    private PacketData data;
}
