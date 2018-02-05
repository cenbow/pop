package com.ai.bdx.pop.kafka;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 






import com.ai.bdx.pop.kafka.reveiver.ICepMessageReceiveService;
import com.ai.bdx.pop.kafka.reveiver.impl.CepMessageReceiverImpl;
import com.ai.bdx.pop.util.CepCONST;
import com.ai.bdx.pop.util.CepUtil;
import com.ai.bdx.pop.util.PopConfigure;
import com.asiainfo.biframe.utils.config.Configure;
import com.jfinal.kit.PropKit;
 

public class CepMessageReceiverThread extends Thread implements Serializable {

	private static final long serialVersionUID = -465446739292851237L;

	private static final Logger log = LogManager.getLogger();
 
 
	private boolean flag = true;
	private String activityCode; // 活动编码
	private Date executeDate;
	private String eventId;
	private String topic;
	private int partitionsNum;

	/**
	 * 构造方法(默认"direct"方式发送消息)
	 * 
	 * @param exchangeName
	 *            消息交换机名称
	 * @param message
	 *            消息
	 * @throws IOException
	 */
	public CepMessageReceiverThread(final String eventId,final String activityCode)  {
		try {
			this.eventId = eventId;
			this.executeDate = Calendar.getInstance().getTime();
			this.activityCode = activityCode;
			this.topic = CepCONST.ROUTE_EPL_RES_PRFIFX + eventId;
			this.partitionsNum = Integer.parseInt(Configure.getInstance().getProperty("KAFKA_PARTITIONS_NUM"));
			this.setName("CepMessageReceiveThread-" + topic );
			CepReceiveThreadCache.getInstance().put(activityCode, this);
		} catch (Exception e) {
			log.error("", e);
		}

	}

	public String getEventId() {
		return this.eventId;
	}

	public Date getExecuteDaste() {
		return executeDate;
	}

	public void stopThread() {
		this.flag = false;
	}

	@Override
	public void run() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(executeDate);
		//零点时间
		calendar.set(Calendar.HOUR, -12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(calendar.DATE, 1);
		long tommorrow = calendar.getTimeInMillis();
		// 开启监听
		try {
			ICepMessageReceiveService executor  = new CepMessageReceiverImpl();
			executor.setActivityCode(activityCode);
			CepKafKaConsumer ckc = new CepKafKaConsumer(topic, partitionsNum,executor);
			ckc.start();
			while (flag) {
				if (System.currentTimeMillis() >= tommorrow) {
					this.stopThread();
					CepUtil.stopCepEvent(this.eventId);
					break;
				}
			}
			// 关闭监听
			ckc.setExit(true);
			ckc.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
