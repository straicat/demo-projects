package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

public class RedisClientRequestHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String message = (String) msg;
        ByteBuf buf = ctx.alloc().buffer(message.length());
        buf.writeBytes(message.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(message);
        ReferenceCountUtil.release(buf);
    }
}
