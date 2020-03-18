package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.*;

import java.nio.ByteBuffer;

public class PacketPlayOut01JoinGame extends PacketData {
    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private Difficulty difficulty;
    private UnsignedByte maxPlayers;
    private LevelType levelType;
    private boolean reducedDebug;

    PacketPlayOut01JoinGame() {
        super(1, "JOIN_GAME", PacketState.PLAY, PacketType.CLIENTBOUND);
    }
    public PacketPlayOut01JoinGame(int entityId, GameMode gameMode, Dimension dimension, Difficulty difficulty, int maxPlayers, LevelType levelType, boolean reducedDebug) {
        this();
        this.entityId = entityId;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = new UnsignedByte(maxPlayers);
        this.levelType = levelType;
        this.reducedDebug = reducedDebug;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] f1 = packetIO.getDataWriter(Integer.class).write(entityId);
        byte[] f2 = packetIO.getDataWriter(UnsignedByte.class).write(new UnsignedByte((int) gameMode.getId()));
        byte[] f3 = packetIO.getDataWriter(Byte.class).write(dimension.getId());
        byte[] f4 = packetIO.getDataWriter(UnsignedByte.class).write(new UnsignedByte((int) difficulty.getId()));
        byte[] f5 = packetIO.getDataWriter(UnsignedByte.class).write(maxPlayers);
        byte[] f6 = packetIO.getDataWriter(String.class).write(levelType.getPacketName());
        byte[] f7 = packetIO.getDataWriter(Boolean.class).write(reducedDebug);

        ByteBuffer bb = ByteBuffer.allocate(f1.length + f2.length + f3.length + f4.length + f5.length + f6.length + f7.length);
        bb.put(f1);
        bb.put(f2);
        bb.put(f3);
        bb.put(f4);
        bb.put(f5);
        bb.put(f6);
        bb.put(f7);
        return bb.array();
    }
}
