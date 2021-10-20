package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RedisResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.readByte();
        // 读取长度
        int len = 0;
        char cur;
        boolean negFlag = false;
        while ((cur = (char) in.readByte()) != '\r') {
            if (cur == '-') {
                negFlag = true;
            }
            len = len * 10 + (cur - '0');
        }
        if (negFlag) {
            len *= -1;
        }

        if (len < 0) {
            out.add(null);
        } else {
            in.readByte();
            CharSequence sequence = in.readCharSequence(len, StandardCharsets.UTF_8);
            String msg = sequence.toString();
            out.add(msg);
        }
        // 跳过结尾的\r\n
        in.readBytes(2);
    }
}
