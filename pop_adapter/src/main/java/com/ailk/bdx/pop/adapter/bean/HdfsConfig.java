package com.ailk.bdx.pop.adapter.bean;

public class HdfsConfig extends BaseConfig{
  
	private String hdfsIp;
	
	private String hdfsPort;
	
	private String hdfsCatalog;
	
	private ScanPeriod scanPeriod;
	
	private int batchNum; //读取批次
	
	private int batchSize; //批次读取数量
	
	//延迟时间 
	private String delayMin;
	
	private String fileSuffix;
	
	private String charsetName;// Hdfs传输编码
	
	private int adapterGnNum; //adapter读取Gn序号
	
	private int adapterGnTotal; //adapter读取Gn部署总数

	public int getAdapterGnNum() {
		return adapterGnNum;
	}

	public void setAdapterGnNum(int adapterGnNum) {
		this.adapterGnNum = adapterGnNum;
	}

	public int getAdapterGnTotal() {
		return adapterGnTotal;
	}

	public void setAdapterGnTotal(int adapterGnTotal) {
		this.adapterGnTotal = adapterGnTotal;
	}

	public String getDelayMin() {
		return delayMin;
	}

	public void setDelayMin(String delayMin) {
		this.delayMin = delayMin;
	}

	public String getHdfsIp() {
		return hdfsIp;
	}

	public void setHdfsIp(String hdfsIp) {
		this.hdfsIp = hdfsIp;
	}

	public String getHdfsPort() {
		return hdfsPort;
	}

	public void setHdfsPort(String hdfsPort) {
		this.hdfsPort = hdfsPort;
	}

	public String getHdfsCatalog() {
		return hdfsCatalog;
	}

	public void setHdfsCatalog(String hdfsCatalog) {
		this.hdfsCatalog = hdfsCatalog;
	}

	public ScanPeriod getScanPeriod() {
		return scanPeriod;
	}

	public void setScanPeriod(ScanPeriod scanPeriod) {
		this.scanPeriod = scanPeriod;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	public int getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	
	
 
}
