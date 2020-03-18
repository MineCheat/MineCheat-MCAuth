package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;
import lombok.Getter;

import java.security.PublicKey;

public class PacketLogin01EncryptionResponse extends PacketData{
    @Getter
    private byte[] shared_secret;

    @Getter
    private byte[] verify_token;

    PacketLogin01EncryptionResponse() {
        super(1, "ENCRYPTION_RESPONSE", PacketState.LOGIN, PacketType.SERVERBOUND);
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {
        int shared_secret_len = packetIO.getDataReader(VarInt.class).read(buffer).getValue();
        shared_secret = new byte[shared_secret_len];
        buffer.readBytes(shared_secret);

        int verify_token_len = packetIO.getDataReader(VarInt.class).read(buffer).getValue();
        verify_token = new byte[verify_token_len];
        buffer.readBytes(verify_token);
    }

    @Override
    public byte[] writePacket() throws Exception {
        return new byte[0];
    }
}
