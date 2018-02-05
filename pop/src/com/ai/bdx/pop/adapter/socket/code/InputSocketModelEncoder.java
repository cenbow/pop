package com.ai.bdx.pop.adapter.socket.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.ai.bdx.pop.adapter.socket.model.InputSocketModel;

public class InputSocketModelEncoder extends ProtocolEncoderAdapter {
	private final Charset charset;
	public InputSocketModelEncoder(Charset charset){
		if(charset==null){
			throw new IllegalArgumentException("charset");
		}
		this.charset = charset;
	}
	@Override
	public void encode(IoSession iosession, Object message,
			ProtocolEncoderOutput out) throws Exception {
		InputSocketModel mc = (InputSocketModel) message;
		IoBuffer buffer = IoBuffer.allocate(1024, false);
		buffer.setAutoExpand(true);
		int len = mc.getLen();
		buffer.putInt(len);  //此长度为字符串的字节码长度
		String msg = mc.getMessage();
	    buffer.putString(msg, charset.newEncoder());
		buffer.flip();
		out.write(buffer);		
	}

}
