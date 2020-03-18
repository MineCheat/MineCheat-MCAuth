package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.Difficulty;
import kr.minecheat.mcauth.mcdata.UnsignedByte;
import lombok.Getter;

public class PacketPlayOut41ServerDifficulty extends PacketData{

    @Getter
    private Difficulty difficulty;

    PacketPlayOut41ServerDifficulty() {
        super(0x41, "SERVER_DIFFICULTY", PacketState.PLAY, PacketType.CLIENTBOUND);
    }
    public PacketPlayOut41ServerDifficulty(Difficulty difficulty) {
        this();
        this.difficulty = difficulty;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(UnsignedByte.class).write(new UnsignedByte((int) difficulty.getId()));
    }
}
