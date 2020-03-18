package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;

public class PacketPlayOut09HeldItemChange extends PacketData {

    private byte slot;

    PacketPlayOut09HeldItemChange() {
        super(0x09, "HELD_ITEM_CHANGE", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut09HeldItemChange(byte slot) {
        this();
        this.slot = slot;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        return packetIO.getDataWriter(Byte.class).write(slot);
    }
}
