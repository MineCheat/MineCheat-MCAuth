package kr.minecheat.mcauth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import kr.minecheat.mcauth.utils.EncryptionUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.util.List;

public class MinecraftPacketEncrypter extends MessageToByteEncoder<ByteBuf> {

    private SecretKey key;
    private Cipher cipher;

    public MinecraftPacketEncrypter(SecretKey key) throws GeneralSecurityException {
        this.key = key;
        this.cipher = EncryptionUtils.keyToCipher(Cipher.ENCRYPT_MODE, key);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf bytes, ByteBuf writeBuffer) throws Exception {

        int i = bytes.readableBytes();
        byte[] read = new byte[i];
        bytes.readBytes(read, 0, i);

        int j = this.cipher.getOutputSize(read.length);
        byte[] write = this.cipher.update(read, 0,read.length);
        writeBuffer.writeBytes(write);
    }


}
