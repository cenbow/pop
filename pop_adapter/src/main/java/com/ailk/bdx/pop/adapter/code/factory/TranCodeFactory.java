package com.ailk.bdx.pop.adapter.code.factory;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ailk.bdx.pop.adapter.code.TranModelDecoder;
import com.ailk.bdx.pop.adapter.code.TranModelEncoder;
 
/**
 * adapter湖北对接pop 编码工厂类
 * @author lyz
 * */
public class TranCodeFactory implements ProtocolCodecFactory{

	private Charset charset;
	
	public TranCodeFactory(Charset charset){
		this.charset = charset;
	}
	
	@Override
	public ProtocolDecoder getDecoder(IoSession iosession) throws Exception {
		return  new TranModelDecoder(charset);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession iosession) throws Exception {
		return new TranModelEncoder(charset);
	}

}
