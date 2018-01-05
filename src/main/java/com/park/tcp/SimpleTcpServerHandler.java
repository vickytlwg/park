package com.park.tcp;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.park.model.Constants;
import com.park.model.PosChargeData;
import com.park.service.PosChargeDataService;
import com.park.service.impl.PosChargeDataServiceImpl;

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
	
	private static Log logger = LogFactory.getLog(SimpleTcpServerHandler.class);
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
		String teString=new String(s.getBytes(),"utf-8");
		System.out.println("接收到数据:"+teString);
		s=teString;
		if (s.length()<5) {
			return;
		}
		Gson gson = new Gson();
		Map<String, Object> mapdata=new HashMap<>();
		try {
			 mapdata=gson.fromJson(s, new TypeToken<Map<String, Object>>(){
		        }.getType() );
		} catch (Exception e) {
			// TODO: handle exception
			incoming.writeAndFlush("{\"status\":"+4001+"}\n");
			return;
		}
				
		 System.out.println("转换成的map:"+mapdata.toString());
		if (s.length()<18&&s.length()>0) {
				 channelsMap.put((String) mapdata.get("parkId"), incoming);
				return;
			}
		if (mapdata.get("parkId")!=null) {
			Constants.tcpReceiveDatas.put((String) mapdata.get("parkId"), mapdata);
			if (mapdata.get("cardNumber")!=null) {
				String cardNumber=(String) mapdata.get("cardNumber");
				try {
					System.out.println("查询账单: "+cardNumber);
					List<PosChargeData> queryCharges= Constants.poschargeSerivce.queryCurrentDebt(cardNumber, new Date());
					
					String status="4000";
					if (!queryCharges.isEmpty()) {
						System.out.println("查询账单结果"+queryCharges.toString());
						PosChargeData posChargeData=queryCharges.get(0);
						String dataSend="{\"parkId\":\""+posChargeData.getParkId()+"\",\"charge\":\""+posChargeData.getChargeMoney()
						+"\",\"cardNumber\":\""+cardNumber+"\",\"status\":"+4000+"}\n";
						incoming.writeAndFlush(dataSend);
					}
					else {
						incoming.writeAndFlush("{\"status\":"+4001+",\"cardNumber\":"+cardNumber+"}\n");
					}
				} catch (Exception e) {
					// TODO: handle exception
					incoming.writeAndFlush("{\"status\":"+4001+",\"cardNumber\":"+cardNumber+"}\n");
				}
			}
			
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
    	         logger.info( ctx.channel().remoteAddress()+"超时类型：" + type);
    		}
    }
    public void sendToAll(String content){
    	channels.writeAndFlush(content+"\n");
    }
  
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
       
        logger.info("SimpleChatClient:"+incoming.remoteAddress()+"在线");	
			
			Collection<Channel> channelsc=channelsMap.values();
			   Iterator it = channelsc.iterator();
		        for (; it.hasNext();) {
		        	logger.info("mapClient:"+((Channel)(it.next())).remoteAddress()+"上线");
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
        logger.info("Client:"+incoming.remoteAddress()+"掉线");
		Collection<Channel> channelsc=channelsMap.values();
		   Iterator it = channelsc.iterator();
	        for (; it.hasNext();) {
	        	logger.info("mapClient:"+((Channel)(it.next())).remoteAddress()+"剩下");
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
         logger.info("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
 
}