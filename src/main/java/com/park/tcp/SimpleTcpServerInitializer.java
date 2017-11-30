package com.park.tcp;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务端 ChannelInitializer
 * 
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleTcpServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final int READ_IDEL_TIME_OUT = 0; // 读超时
	private static final int WRITE_IDEL_TIME_OUT = 0;// 写超时
	private static final int ALL_IDEL_TIME_OUT = 60; // 所有超时

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// ByteBuf delimiter = Unpooled.copiedBuffer("\t".getBytes());
		pipeline.addLast(
				new IdleStateHandler(READ_IDEL_TIME_OUT, WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder(Charset.forName("utf-8")));
		pipeline.addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
		pipeline.addLast("handler", new SimpleTcpServerHandler());
	
		System.out.println("SimpleChatClient:" + ch.remoteAddress() + "连接上");
	}
}
