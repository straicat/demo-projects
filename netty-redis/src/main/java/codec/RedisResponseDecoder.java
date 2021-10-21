package codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class RedisResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte start = in.readByte();
        if (start == '$') {
            // 读取长度
            int len = 0;
            char cur;
            while ((cur = (char) in.readByte()) != '\r') {
                if (cur == '-') {
                    // 说明是空值，跳过后续的1\r\n
                    out.add(Optional.empty());
                    in.readBytes(3);
                    return;
                }
                len = len * 10 + (cur - '0');
            }
            // 当前cur位于\r，跳过后面的\n
            in.readByte();

            CharSequence sequence = in.readCharSequence(len, StandardCharsets.UTF_8);
            String msg = sequence.toString();
            out.add(Optional.of("\"" + msg + "\""));
            // 跳过结尾的\r\n
            in.readBytes(2);
        } else {
            // 单行字符串
            int index = ByteBufUtil.indexOf(Unpooled.buffer(1).writeByte('\r'), in);
            // 注意这里的消息格式应与上面的保持一致为Optional<String>
            if (index > 1) {
                out.add(Optional.of(in.readCharSequence(index - 1, StandardCharsets.UTF_8).toString()));
            } else {
                out.add(Optional.empty());
            }
            // 跳过结尾的\r\n
            in.readBytes(2);
        }
    }
}
