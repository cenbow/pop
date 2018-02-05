package com.ai.bdx.pop.adapter.socket.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.ai.bdx.pop.adapter.socket.buffer.PopBuffer;
import com.ai.bdx.pop.adapter.socket.model.InputSocketModel;

/**
 * @description TODO
 * @author lyz
 */

public class InputSocketHandler extends IoHandlerAdapter {

	private static Logger log = LogManager.getLogger();

	public void sessionClosed(IoSession session) throws Exception {
		log.error("会话关闭!");
		session.close(true);
	}

	/**
	 * 处理过程中有异常出现的时候会被触发 将会简单打印异常的一些信息并关闭该session，对于大多数应用这些足够使用了
	 * 如果需要一些特殊处理可以编写该方法
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error("连接出现异常：" + session.getRemoteAddress(), cause);
		session.close(true);
	}

	/**
	 * If the message is 'quit', we exit by closing the session. Otherwise, we
	 * return the current date. 该方法将接受来之客户端的数据，并将当前时间返回给客户端，如果客户端消息是“quit”
	 * 则关闭session 值得注意的是该方法的第二个参数主要依赖于 配置的协议编解码器你所使用的，使用的协议编码器不同
	 * 则该参数返回的数据类型也不会不同，如果没有配置，则会接收到一个IoBuffer类型数据
	 * 
	 */
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		InputSocketModel mc = (InputSocketModel) message;
		//esper客户端连接, 将该会话保存
		//String mess=(String) message;
		log.debug("++++++++++POP收到消息" + mc.getMessage());
		PopBuffer.getInstance().publish(mc.getMessage());

	}

	/**
	 * On idle, we just write a message on the console
	 * 该方法被调用一旦一个session闲置了一段时间后时间配置在 acceptor.getSessionConfig().setIdleTime(
	 * IdleStatus.BOTH_IDLE, 10 ); 单位是秒
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		//System.out.println("IDLE " + session.getIdleCount(status));
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		log.info("Adapter转换服务器连接成功：" + session.getRemoteAddress());
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub  

	}

}
