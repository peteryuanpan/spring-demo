package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ByteBuf buf = Unpooled.copiedBuffer("[客户端]" + channel.remoteAddress() + "上线了", CharsetUtil.UTF_8);
        channelGroup.writeAndFlush(buf);
        channelGroup.add(channel);
        System.out.println(channel.remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channelGroup.remove(channel);
        ByteBuf buf = Unpooled.copiedBuffer("[客户端]" + channel.remoteAddress() + "下线了", CharsetUtil.UTF_8);
        channelGroup.writeAndFlush(buf);
        System.out.println(channel.remoteAddress() + "下线了");
    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象，含有通道channel，管道pipeline
     * @param msg 就是客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务端读取线程 " + Thread.currentThread().getName());
        // Channel channel = ctx.channel();
        // ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链接，出站入站
        // 将 msg 转成一个 ByteBuf，类似 NIO 的 ByteBuffer

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端的发送消息是：" + buf.toString(CharsetUtil.UTF_8));

        for (Channel channel : channelGroup) {
            if (!channel.equals(ctx.channel())) {
                channel.writeAndFlush(buf);
            }
        }
    }

    /**
     * 处理异常，一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
