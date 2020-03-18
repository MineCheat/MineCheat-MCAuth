package kr.minecheat.mcauth.packets;

import java.util.*;

public class PacketDataRegistry {
    private static final HashMap<Integer, Class<PacketData>[]> readablePackets = new HashMap<>();
    private static final HashMap<Integer, List<Class<PacketData>>> allPackets = new HashMap<>();


    static {
        register(new PacketStatus00ServerListPing());
        register(new PacketStatus00ServerListPingResponse());
        register(new PacketLogin00Disconnect());
        register(new PacketStatus01JustAPing());
        register(new PacketLogin00LoginStart());
        register(new PacketLogin01EncryptionRequest());
        register(new PacketLogin01EncryptionResponse());
        register(new PacketLogin03SetCompression());
        register(new PacketPlay00KeepAlive());
        register(new PacketPlayOut01JoinGame());
        register(new PacketPlayOut3FPluginChannel());
        register(new PacketPlayOut05SpawnPosition());
        register(new PacketPlayOut39PlayerAbilities());
        register(new PacketPlayOut40Disconnect());
        register(new PacketPlayOut41ServerDifficulty());
        register(new PacketPlayOut02Chat());
        register(new PacketPlayOut08PlayerPositionAndLook());
        register(new PacketPlayOut09HeldItemChange());
        register(new PacketPlayOut38PlayerListItem());
        register(new PacketPlayOut26MapChunkBulkButDeveloperIsLazyAndRefusingToImplement());
        register(new PacketPlayIn01ChatMessage());
    }

    private static void register(PacketData packet) {
        if (allPackets.containsKey(packet.getPacketID())) {
            allPackets.get(packet.getPacketID()).add((Class<PacketData>) packet.getClass());
        } else {
            allPackets.put(packet.getPacketID(), new ArrayList<>(Arrays.asList((Class < PacketData >) packet.getClass())));
        }

        if (packet.getPacketType() != PacketType.CLIENTBOUND) {
            if (!readablePackets.containsKey(packet.getPacketID())) {
                readablePackets.put(packet.getPacketID(), new Class[3]);
            }
            readablePackets.get(packet.getPacketID())[packet.getPacketState().ordinal()] = (Class<PacketData>) packet.getClass();
        }
    }

    public static PacketData getPacketData(PacketHeader header, PacketState state) throws IllegalAccessException, InstantiationException {
        Class<PacketData>[] dataList = readablePackets.get(header.getPacketId());
        Class<PacketData> data = dataList[state.ordinal()];
        Objects.requireNonNull(data);
        return data.newInstance();
    }
}
