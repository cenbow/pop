package com.ailk.bdx.pop.adapter.code;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ailk.bdx.pop.adapter.model.TranModel;


/**
 * adapter直接对接pop,编码类
 * @author lyz
 * */
public class TranModelEncoder extends ProtocolEncoderAdapter {
	private final Charset charset;
	private static final Logger log = LogManager.getLogger();
 
	public TranModelEncoder(Charset charset){
		if(charset==null){
			throw new IllegalArgumentException("charset");
		}
		this.charset = charset;
	}
	@Override
	public void encode(IoSession iosession, Object message,
			ProtocolEncoderOutput out) throws Exception {
		TranModel mc = (TranModel) message;
		IoBuffer.setUseDirectBuffer(false);    
		IoBuffer buffer = IoBuffer.allocate(8,false);
		buffer.setAutoExpand(true);
		buffer.setAutoShrink(true);//自动收缩
		int len = mc.getLen();
		buffer.putInt(len);  //此长度为字符串的字节码长度
		String msg = mc.getMessage();
	//	log.debug("+++++当前线程名:"+Thread.currentThread().getName()+"+++++++++发给pop编码内容:"+msg);
	    buffer.putString(msg, charset.newEncoder());
		buffer.flip();
		buffer.free();
	 
		out.write(buffer);		
	}

}
