package com.ai.bdx.pop.adapter.socket.code.factory;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ai.bdx.pop.adapter.socket.code.InputSocketModelDecoder;
import com.ai.bdx.pop.adapter.socket.code.InputSocketModelEncoder;
 

 

public class InputSocketCodeFactory implements ProtocolCodecFactory{

	private Charset charset;
	
	public InputSocketCodeFactory(Charset charset){
		this.charset = charset;
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession iosession) throws Exception {
		return  new InputSocketModelDecoder(charset);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession iosession) throws Exception {
		return new InputSocketModelEncoder(charset);
	}

}
