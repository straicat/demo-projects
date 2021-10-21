package codec;

import domain.RedisResponse;
import enums.ValueTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RedisServerCodec extends ByteToMessageCodec<RedisResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RedisResponse msg, ByteBuf out) throws Exception {
        if (msg.getValueType() == ValueTypeEnum.MULTI_LINE_STRING) {
            out.writeByte('$');
            if (msg.getValue() == null) {
                out.writeCharSequence("-1", StandardCharsets.UTF_8);
            } else {
                String value = msg.getValue().toString();
                out.writeCharSequence(String.valueOf(value.length()), StandardCharsets.UTF_8);
                out.writeByte('\r').writeByte('\n');
                out.writeCharSequence(value, StandardCharsets.UTF_8);
            }
        } else {
            if (msg.getValueType() == ValueTypeEnum.ONE_LINE_STRING) {
                out.writeByte('+');
            } else if (msg.getValueType() == ValueTypeEnum.ONE_LINE_MESSAGE) {
                out.writeByte('-');
            }
            if (msg.getValue() != null) {
                out.writeCharSequence(msg.getValue().toString(), StandardCharsets.UTF_8);
            }
        }
        out.writeByte('\r').writeByte('\n');
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.skipBytes(1);
        int size = 0;
        byte cur;
        while ((cur = in.readByte()) != '\r') {
            size = size * 10 + cur - '0';
        }
        in.skipBytes(1);
        String[] cmds = new String[size];
        for (int i = 0; i < size; i++) {
            in.skipBytes(1);
            int len = 0;
            byte c;
            while ((c = in.readByte()) != '\r') {
                len = len * 10 + c - '0';
            }
            in.skipBytes(1);
            String cmd = in.readCharSequence(len, StandardCharsets.UTF_8).toString();
            cmds[i] = cmd;
            in.skipBytes(2);
        }
        out.add(cmds);
    }
}
