package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class RedisRequestEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        String[] cmds = msg.split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(cmds.length).append("\r\n");
        for (String cmd : cmds) {
            sb.append("$").append(cmd.length()).append("\r\n")
                    .append(cmd).append("\r\n");
        }
        out.writeBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
