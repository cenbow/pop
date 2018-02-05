package com.ai.bdx.pop.util.ftp;


public class FtpConfig {
	private String FTP_ADDRESS;//FTP服务器地址
	private String FTP_PORT;//端口
	private String FTP_PATH;//服务器路径
	private String LOCAL_PATH;//本地文件路径
	private String FTP_FILENAME;//ftp文件名
	private String FTP_USER;//用户名
	private String FTP_PASSWORD;//密码
	private String FTP_PASSWORD_ENCRYPT;//密码是否加密（0-不加密;1-加密）
	private String FTP_ENCODE;//ftp传输编码
	private String ISDELETE_LOCAL;//是否删除本地文件（上传或下载后，是否删除本地生成的文件）（0-不删;1-删）
	private String ISDELETE_REMOTE;//是否删除远程文件（下载后，是否删除本地生成的文件）（0-不删;1-删）
	private String JK_CODE;//接口编码
	
	private String FILEPREFIX;//文件名签到字符串
	private String FILETIMEFORMAT;//文件名时间串
	private String FILESUFFIX;//文件名后缀
	private String BACKFILESUFFIX;//反馈文件名后缀
	private String SPLIT;//文本分割符
	private String PERIOD;//间隔
	private String EVENTID;//事件源ID
	private String UPLOAD_LOCAL_PATH;//本地上传文件路径
	private String UPLOAD_REMOTE_PATH;//远程上传到BOSS文件路径
	private String MOVE_PATH;//移动文件路径
	private String ERROR_PATH; //错误文件路径
	public String getFTP_ADDRESS() {
		return FTP_ADDRESS;
	}
	public void setFTP_ADDRESS(String fTP_ADDRESS) {
		FTP_ADDRESS = fTP_ADDRESS;
	}
	public String getFTP_PORT() {
		return FTP_PORT;
	}
	public void setFTP_PORT(String fTP_PORT) {
		FTP_PORT = fTP_PORT;
	}
	public String getFTP_PATH() {
		return FTP_PATH;
	}
	public void setFTP_PATH(String fTP_PATH) {
		FTP_PATH = fTP_PATH;
	}
	public String getLOCAL_PATH() {
		return LOCAL_PATH;
	}
	public void setLOCAL_PATH(String lOCAL_PATH) {
		LOCAL_PATH = lOCAL_PATH;
	}
	public String getFTP_FILENAME() {
		return FTP_FILENAME;
	}
	public void setFTP_FILENAME(String fTP_FILENAME) {
		FTP_FILENAME = fTP_FILENAME;
	}
	public String getFTP_USER() {
		return FTP_USER;
	}
	public void setFTP_USER(String fTP_USER) {
		FTP_USER = fTP_USER;
	}
	public String getFTP_PASSWORD() {
		return FTP_PASSWORD;
	}
	public void setFTP_PASSWORD(String fTP_PASSWORD) {
		FTP_PASSWORD = fTP_PASSWORD;
	}
	public String getFTP_PASSWORD_ENCRYPT() {
		return FTP_PASSWORD_ENCRYPT;
	}
	public void setFTP_PASSWORD_ENCRYPT(String fTP_PASSWORD_ENCRYPT) {
		FTP_PASSWORD_ENCRYPT = fTP_PASSWORD_ENCRYPT;
	}
	public String getFTP_ENCODE() {
		return FTP_ENCODE;
	}
	public void setFTP_ENCODE(String fTP_ENCODE) {
		FTP_ENCODE = fTP_ENCODE;
	}
	public String getISDELETE_LOCAL() {
		return ISDELETE_LOCAL;
	}
	public void setISDELETE_LOCAL(String iSDELETE_LOCAL) {
		ISDELETE_LOCAL = iSDELETE_LOCAL;
	}
	public String getISDELETE_REMOTE() {
		return ISDELETE_REMOTE;
	}
	public void setISDELETE_REMOTE(String iSDELETE_REMOTE) {
		ISDELETE_REMOTE = iSDELETE_REMOTE;
	}
	public String getJK_CODE() {
		return JK_CODE;
	}
	public void setJK_CODE(String jK_CODE) {
		JK_CODE = jK_CODE;
	}
	public String getFILEPREFIX() {
		return FILEPREFIX;
	}
	public void setFILEPREFIX(String fILEPREFIX) {
		FILEPREFIX = fILEPREFIX;
	}
	public String getFILETIMEFORMAT() {
		return FILETIMEFORMAT;
	}
	public void setFILETIMEFORMAT(String fILETIMEFORMAT) {
		FILETIMEFORMAT = fILETIMEFORMAT;
	}
	public String getFILESUFFIX() {
		return FILESUFFIX;
	}
	public void setFILESUFFIX(String fILESUFFIX) {
		FILESUFFIX = fILESUFFIX;
	}
	public String getBACKFILESUFFIX() {
		return BACKFILESUFFIX;
	}
	public void setBACKFILESUFFIX(String bACKFILESUFFIX) {
		BACKFILESUFFIX = bACKFILESUFFIX;
	}
	public String getSPLIT() {
		return SPLIT;
	}
	public void setSPLIT(String sPLIT) {
		SPLIT = sPLIT;
	}
	public String getPERIOD() {
		return PERIOD;
	}
	public void setPERIOD(String pERIOD) {
		PERIOD = pERIOD;
	}
	public String getEVENTID() {
		return EVENTID;
	}
	public void setEVENTID(String eVENTID) {
		EVENTID = eVENTID;
	}
	public String getUPLOAD_LOCAL_PATH() {
		return UPLOAD_LOCAL_PATH;
	}
	public void setUPLOAD_LOCAL_PATH(String uPLOAD_LOCAL_PATH) {
		UPLOAD_LOCAL_PATH = uPLOAD_LOCAL_PATH;
	}
	public String getUPLOAD_REMOTE_PATH() {
		return UPLOAD_REMOTE_PATH;
	}
	public void setUPLOAD_REMOTE_PATH(String uPLOAD_REMOTE_PATH) {
		UPLOAD_REMOTE_PATH = uPLOAD_REMOTE_PATH;
	}
	public String getMOVE_PATH() {
		return MOVE_PATH;
	}
	public void setMOVE_PATH(String mOVE_PATH) {
		MOVE_PATH = mOVE_PATH;
	}
	public String getERROR_PATH() {
		return ERROR_PATH;
	}
	public void setERROR_PATH(String eRROR_PATH) {
		ERROR_PATH = eRROR_PATH;
	}
	


	
}
