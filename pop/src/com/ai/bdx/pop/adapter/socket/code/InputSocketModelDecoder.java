package com.ai.bdx.pop.adapter.socket.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ai.bdx.pop.adapter.socket.model.InputSocketModel;
 

public class InputSocketModelDecoder  extends CumulativeProtocolDecoder{
	private final Charset charset; 
	public InputSocketModelDecoder(Charset charset){
		if (charset == null) {
    		throw new IllegalArgumentException("charset");
    	}
    	this.charset = charset;
	}
	
	@Override
	protected boolean doDecode(IoSession iosession, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		int position = in.position();//取得初始位置
		if (in.remaining() >= 1) {
			InputSocketModel mc = new InputSocketModel();
			int a =0;
			try{
			  a = in.getInt();
			}catch(Exception e){
				in.position(position);
				return false;
			}
			if(a>in.remaining()){
				// 消息内容断包,返回原始位置
				in.position(position);
				return false;
			}
			mc.setLen(a);
			mc.setMessage(in.getString(a,charset.newDecoder()));
			out.write(mc);
            return true;
        } else {
            return false;
        }
		 
	}
}
