package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.MojangUser;
import kr.minecheat.mcauth.mcdata.PlayerListItem;
import kr.minecheat.mcauth.mcdata.VarInt;

import java.nio.ByteBuffer;

public class PacketPlayOut38PlayerListItem extends PacketData {

    private PlayerListItem playerListItem;

    PacketPlayOut38PlayerListItem() {
        super(0x38, "PLAYER_LIST_ITEM", PacketState.PLAY, PacketType.CLIENTBOUND);
    }

    public PacketPlayOut38PlayerListItem(PlayerListItem playerListItem) {
        this();
        this.playerListItem = playerListItem;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {

        PlayerListItem.Action action = playerListItem.getAction();

        byte[] f1 = packetIO.getDataWriter(VarInt.class).write(new VarInt(action.getId()));
        byte[] f2 = packetIO.getDataWriter(VarInt.class).write(new VarInt(playerListItem.getActions().size()));


        int lengths = 0;
        byte[][] f3 = new byte[playerListItem.getActions().size()][];
        for (int i = 0 ; i < f3.length; i++) {
            PlayerListItem.ActionValue actionValue = playerListItem.getActions().get(i);
            if (actionValue.getAction() != action) throw new Exception("who made this mess");

            byte[] uid = packetIO.getDataWriter(java.util.UUID.class).write(actionValue.getUuid());
            byte[] data = new byte[0];
            if (actionValue instanceof PlayerListItem.ActionValue.AddPlayer) {
                PlayerListItem.ActionValue.AddPlayer add = (PlayerListItem.ActionValue.AddPlayer) actionValue;
                byte[] v1 = packetIO.getDataWriter(String.class).write(add.getName());
                byte[] v2 = packetIO.getDataWriter(VarInt.class).write(new VarInt(add.getProperties().size()));
                byte[][] v3 = new byte[add.getProperties().size()][];
                int totalV3Length = 0;
                for (int j = 0; j <v3.length; j++) {
                    MojangUser.MojangUserProperties prop = add.getProperties().get(j);
                    byte[] p1 = packetIO.getDataWriter(String.class).write(prop.getName());
                    byte[] p2 = packetIO.getDataWriter(String.class).write(prop.getValue());
                    byte[] p3 = packetIO.getDataWriter(Boolean.class).write(prop.getSignature() != null);
                    byte[] p4 = new byte[0];
                    if (prop.getSignature() != null) p4 = packetIO.getDataWriter(String.class).write(prop.getSignature());
                    ByteBuffer buf = ByteBuffer.allocate(p1.length + p2.length + p3.length + p4.length);
                    buf.put(p1);
                    buf.put(p2);
                    buf.put(p3);
                    buf.put(p4);
                    v3[i] = buf.array();
                    totalV3Length += v3[i].length;
                }
                byte[] v4 = packetIO.getDataWriter(VarInt.class).write(new VarInt((int) add.getGameMode().getId()));
                byte[] v5 = packetIO.getDataWriter(VarInt.class).write(new VarInt(add.getPing()));
                byte[] v6 = packetIO.getDataWriter(Boolean.class).write(add.getDisplayName() != null);
                byte[] v7 = new byte[0];
                if (add.getDisplayName() != null) v7 = packetIO.getDataWriter(String.class).write(add.getDisplayName());

                ByteBuffer buf = ByteBuffer.allocate(v1.length + v2.length + totalV3Length + v4.length + v5.length + v6.length + v7.length);
                buf.put(v1);
                buf.put(v2);
                for (int j = 0 ; j < v3.length ; j++)
                    buf.put(v3[j]);
                buf.put(v4);
                buf.put(v5);
                buf.put(v6);
                buf.put(v7);
                data = buf.array();
            } else if (actionValue instanceof PlayerListItem.ActionValue.UpdateGamemode) {
                data = packetIO.getDataWriter(VarInt.class).write(new VarInt((int) ((PlayerListItem.ActionValue.UpdateGamemode) actionValue).getGameMode().getId()));
            } else if (actionValue instanceof PlayerListItem.ActionValue.UpdatePing) {
                data = packetIO.getDataWriter(VarInt.class).write(new VarInt(((PlayerListItem.ActionValue.UpdatePing) actionValue).getPing()));
            } else if (actionValue instanceof PlayerListItem.ActionValue.UpdateDisplayName) {
                byte[] v1 = packetIO.getDataWriter(Boolean.class).write(((PlayerListItem.ActionValue.UpdateDisplayName) actionValue).getDisplayName() != null);
                byte[] v2 = new byte[0];
                if (((PlayerListItem.ActionValue.UpdateDisplayName) actionValue).getDisplayName() != null)
                    v2 = packetIO.getDataWriter(String.class).write(((PlayerListItem.ActionValue.UpdateDisplayName) actionValue).getDisplayName());
                ByteBuffer buf = ByteBuffer.allocate(v1.length + v2.length);
                buf.put(v1);
                buf.put(v2);
                data = buf.array();
            }

            ByteBuffer buf = ByteBuffer.allocate(uid.length + data.length);
            buf.put(uid);
            buf.put(data);
            f3[i] = buf.array();
            lengths += f3[i].length;
        }

        ByteBuffer finalBuf = ByteBuffer.allocate(f1.length + f2.length + lengths);
        finalBuf.put(f1);
        finalBuf.put(f2);
        for (int i = 0 ; i < f3.length; i++)
            finalBuf.put(f3[i]);

        return finalBuf.array();
    }
}
