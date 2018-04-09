package com.ailk.bdx.pop.adapter.code.factory;

import java.lang.reflect.Constructor;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import com.ailk.bdx.pop.adapter.util.Configure;
 
/**
 * adapter mina 编解码工厂类
 * @author lyz
 * */
public class McCdrCodecFactory implements ProtocolCodecFactory{

	private Charset charset;
	private String decoderClass="com.ailk.bdx.cep.adapter.code.impl.province.CdrDetailDecoder";
	private String encoderClass="com.ailk.bdx.cep.adapter.code.impl.province.CdrDetailEncoder";
	
	public McCdrCodecFactory(Charset charset){
		this.charset = charset;
		String provice = Configure.getInstance().getProperty("PROVINCE");
		decoderClass = decoderClass.replace("province", provice);
		encoderClass = encoderClass.replace("province", provice);
	}
	
	
	public ProtocolDecoder getDecoder(IoSession iosession) throws Exception {
		Class c =Class.forName(decoderClass);
		Constructor c1=c.getDeclaredConstructor(new Class[]{Charset.class});   
        c1.setAccessible(true);   
        ProtocolDecoder protocolDecoder=(ProtocolDecoder)c1.newInstance(new Object[]{charset});  
		return  protocolDecoder;
	}

	public ProtocolEncoder getEncoder(IoSession iosession) throws Exception {
		Class c =Class.forName(encoderClass);
		Constructor c1=c.getDeclaredConstructor(new Class[]{Charset.class});   
        c1.setAccessible(true);   
        ProtocolEncoder protocolEncoder=(ProtocolEncoder)c1.newInstance(new Object[]{charset});  
		return  protocolEncoder;
	}

}
