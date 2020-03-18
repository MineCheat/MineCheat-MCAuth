package kr.minecheat.mcauth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import kr.minecheat.mcauth.utils.EncryptionUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.util.List;

public class MinecraftPacketDecrypter extends MessageToMessageDecoder<ByteBuf> {

    private SecretKey key;
    private Cipher cipher;


    public MinecraftPacketDecrypter(SecretKey key) throws GeneralSecurityException {
        this.key = key;
        this.cipher = EncryptionUtils.keyToCipher(Cipher.DECRYPT_MODE, key);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buffer, List list) throws Exception {
        int i = buffer.readableBytes();
        byte[] read = new byte[i];
        buffer.readBytes(read, 0, i);
        ByteBuf byteBuf = channelHandlerContext.alloc().heapBuffer(this.cipher.getOutputSize(i));
        int index = this.cipher.update(read, 0, i, byteBuf.array(), byteBuf.arrayOffset());
        byteBuf.writerIndex(index);
        list.add(byteBuf);
    }
}
