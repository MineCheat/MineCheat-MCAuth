package kr.minecheat.mcauth.packets;

import io.netty.buffer.ByteBuf;
import kr.minecheat.mcauth.mcdata.VarInt;

import java.nio.ByteBuffer;
import java.security.PublicKey;

public class PacketLogin01EncryptionRequest extends PacketData {

    private PublicKey pk;

    private byte[] verify_token;


    PacketLogin01EncryptionRequest() {
        super(01, "ENCRYPTION_REQUEST", PacketState.LOGIN, PacketType.CLIENTBOUND);
    }

    public PacketLogin01EncryptionRequest(PublicKey publicKey, byte[] verify_token) {
        this();
        this.pk = publicKey;
        this.verify_token = verify_token;
    }

    @Override
    public void readPacket(ByteBuf buffer, PacketHeader header) throws Exception {

    }

    @Override
    public byte[] writePacket() throws Exception {
        byte[] serverID = packetIO.getDataWriter(String.class).write("");
        byte[] publicKey = pk.getEncoded();
        byte[] publicKeyLen = packetIO.getDataWriter(VarInt.class).write(new VarInt(publicKey.length));
        byte[] verifyToken = verify_token;
        byte[] verifyTokenLen = packetIO.getDataWriter(VarInt.class).write(new VarInt(verifyToken.length));

        ByteBuffer byteBuf = ByteBuffer.allocate(serverID.length + publicKey.length + publicKeyLen.length + verifyToken.length + verifyTokenLen.length);
        byteBuf.put(serverID);
        byteBuf.put(publicKeyLen);
        byteBuf.put(publicKey);
        byteBuf.put(verifyTokenLen);
        byteBuf.put(verifyToken);

        return byteBuf.array();
    }
}
