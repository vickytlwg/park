package com.park.tcp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.park.model.Constants;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 服务端 channel
 * 
 * @author waylau.com
 * @date 2015-2-16
 */
public class SimpleTcpServerHandler extends SimpleChannelInboundHandler<String> { // (1)
	
	/**
	 * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
	 * A closed Channel is automatically removed from the collection,
	 */
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	public static Map<String, Channel> channelsMap=new HashMap<>();
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
       
        // Broadcast a message to multiple Channels
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        
        // Broadcast a message to multiple Channels
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");
        
        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }
//    @Override
//   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//	   System.out.println("messageCome");
//	   Channel incoming = ctx.channel();
//	   String s = (String)msg;
//		
//		System.out.println(s);
//		for (Channel channel : channels) {
//           if (channel != incoming){
//               channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + s + "\n");
//           } else {
//           	channel.writeAndFlush("[you]" + s + "\n");
//           }
//       }
//   };
//   @Override
//	protected void channelRead0(ChannelHandlerContext ctx,
//			TextWebSocketFrame msg) throws Exception { // (1)
//	   System.out.println("messageCome");
//		Channel incoming = ctx.channel();
//		for (Channel channel : channels) {
//           if (channel != incoming){
//               channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
//           } else {
//           	channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
//           }
//       }
//	}
    @Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception { // (4)
		Channel incoming = ctx.channel();
		System.out.println("messageCome");
		System.out.println(s);	
		if (s.length()<5) {
			return;
		}
		Gson gson = new Gson();
		 Map<String, Object> mapdata=gson.fromJson(s, new TypeToken<Map<String, Object>>(){
        }.getType() );		
		if (s.length()<18&&s.length()>0) {
				 channelsMap.put((String) mapdata.get("parkId"), incoming);
				return;
			}
		if (mapdata.get("parkId")!=null) {
			Constants.tcpReceiveDatas.put((String) mapdata.get("parkId"), mapdata);
		}
	}
    @Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {  
    		Channel incoming = ctx.channel();
    		if (evt instanceof IdleStateEvent) {
    			IdleStateEvent event = (IdleStateEvent) evt;
    			String type = "";
    			if (event.state() == IdleState.READER_IDLE) {
    				type = "read idle";
    			} else if (event.state() == IdleState.WRITER_IDLE) {
    				type = "write idle";
    			} else if (event.state() == IdleState.ALL_IDLE) {
    				type = "all idle";
    			}   			
    			incoming.writeAndFlush("disconnect\n");
    			
    			 Set<String> key = channelsMap.keySet();
    	         for (Iterator it = key.iterator(); it.hasNext();) {
    	             String s = (String) it.next();
    	             if (channelsMap.get(s)==incoming) {
    	 				channelsMap.remove(s);
    	 				break;
    	 			}
    	         }
    	         incoming.close();
    			System.out.println( ctx.channel().remoteAddress()+"超时类型：" + type);
    		}
    }
    public void sendToAll(String content){
    	channels.writeAndFlush(content+"\n");
    }
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
       
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"在线");	
			
			Collection<Channel> channelsc=channelsMap.values();
			   Iterator it = channelsc.iterator();
		        for (; it.hasNext();) {
		        	System.out.println("mapClient:"+((Channel)(it.next())).remoteAddress()+"上线");
		        }
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        Set<String> key = channelsMap.keySet();
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            if (channelsMap.get(s)==incoming) {
				channelsMap.remove(s);
				break;
			}
        }
		System.out.println("Client:"+incoming.remoteAddress()+"掉线");
		Collection<Channel> channelsc=channelsMap.values();
		   Iterator it = channelsc.iterator();
	        for (; it.hasNext();) {
	        	System.out.println("mapClient:"+((Channel)(it.next())).remoteAddress()+"剩下");
	        }
	}
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
    	Channel incoming = ctx.channel();
    	 Set<String> key = channelsMap.keySet();
         for (Iterator it = key.iterator(); it.hasNext();) {
             String s = (String) it.next();
             if (channelsMap.get(s)==incoming) {
 				channelsMap.remove(s);
 				break;
 			}
         }
		System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
 
}