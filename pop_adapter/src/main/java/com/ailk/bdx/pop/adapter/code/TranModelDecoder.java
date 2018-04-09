package com.ailk.bdx.pop.adapter.code;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ailk.bdx.pop.adapter.model.TranModel;
/**
 * adapter直接对接pop,解码类
 * @author lyz
 * */
public class TranModelDecoder  extends CumulativeProtocolDecoder{
	private final Charset charset; 
	public TranModelDecoder(Charset charset){
		if (charset == null) {
    		throw new IllegalArgumentException("charset");
    	}
    	this.charset = charset;
	}
	
	@Override
	protected boolean doDecode(IoSession iosession, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() >= 1) {
			TranModel mc = new TranModel();
			int a = in.getInt();
			mc.setLen(a);
			mc.setMessage(in.getString(a,charset.newDecoder()));
			out.write(mc);
            return true;
        } else {
            return false;
        }
		 
	}
}
