package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class RedisRequestHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String cmd = msg + "\n";
        ByteBuf buf = ctx.alloc().buffer(cmd.length());
        buf.writeBytes(cmd.getBytes(StandardCharsets.UTF_8));
        ctx.write(buf, promise);
    }
}
