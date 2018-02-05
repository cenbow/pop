package com.ai.bdx.pop.task;

import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.service.BatchStationService;
import com.ai.bdx.pop.util.SpringContext;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/**
 * 
 * 批量扫描基站变更割接
 * */
public class ScanBatchStationChange {
	private static Logger log = LogManager.getLogger();
	
	public void scanStation() {
		BatchStationService batchStationService = null;
		List<BatchStationBean> beanList = null;
		try{
			batchStationService=SpringContext.getBean("batchStationService",BatchStationService.class);
			//从数据库获取 数据
			String sql = "select * from []";
			List<Record> rs = Db.find(sql);
			for(Record r:rs)
			{
				BatchStationBean bs = new BatchStationBean();
				bs.setSubsid(r.getStr(""));
				bs.setProductNo(r.getStr(""));
				bs.setUserlocationOld(r.getStr(""));
				bs.setUserlocationNew(r.getStr(""));
				bs.setLac_ci_hex_code_old(r.getStr(""));
				bs.setLac_ci_hex_code_new(r.getStr(""));
				beanList.add(bs);
			}
			//beanList = //getBatchStation2Bean();
			//List<String> ls = batchStationService.cpeBatchStationChange(beanList);
		}catch(Exception e){
			log.error(""+e);
		}
	}
	public static void main(String[] args) {
	}
}
