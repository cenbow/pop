package com.asiainfo.biapp.pop.util.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.net.ftp.FTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

/**
 * creator：sjw 
 * create date：2015年4月23日
 *  useful： 文件处理类 
 *  modify
 * ===================================================
 *  person    date      reason
 *  
 * ===================================================
 */
public class FtpFileUtil {
	private static final Logger log = LogManager.getLogger();
	public File file;
	private boolean deleteFile;
	private String charsetName;
	
	public FtpFileUtil(String path,boolean isDeleteFile,String charsetName){
		this.file=new File(path);
		this.deleteFile=isDeleteFile;
		this.setCharsetName(charsetName);
	}
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public void readFileByLine() {
//		Stopwatch timeWatch = Stopwatch.createStarted();
//		try {
//			Files.readLines(file, Charset.forName(charsetName),
//					new LineProcessor() {
//						@Override
//						public boolean processLine(String line)throws IOException {
//							String[]  arrs=line.split(",");
//							//待处理处理行字符分割
//						//	System.out.println("分割开户文件字段信息");
//							String ruleId="";
//						    String taskId="";
//							try {
//								boolean put = CpeInstallCacheQueue.put(arrs[0],taskId,arrs);
//							//	System.out.println("放入队列了吗？"+put);
//							} catch (InterruptedException e) {
//								log.error("put CpeInstallCacheQueue error,{}",e);
//							}
//							return true;
//						}
//						@Override
//						public List<String[]> getResult() {
//							return null;
//						}
//					});
//
//		}catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			log.info("文件读取完毕{},共计" + timeWatch.elapsed(TimeUnit.SECONDS) + "秒",file.getName());
//			if (deleteFile) {
//				file.delete(); // 临时文件删掉
//			}
//		}
//	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isDeleteFile() {
		return deleteFile;
	}

	public void setDeleteFile(boolean deleteFile) {
		this.deleteFile = deleteFile;
	}

	public String getCharsetName() {
		return charsetName;
	}

	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}
	public void readFileByLine2(){
		Reader rd=null;
		BufferedReader br=null;
		String line=null;
		
		try {
			rd=new InputStreamReader(new FileInputStream(file),"utf-8");
			br=new BufferedReader(rd);
			if((line=br.readLine())!=null){
				
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}catch (Exception e) {
		
			e.printStackTrace();
		}
	}
	/**
	 * 销户同步数据成功生成上传路径+文件名
	 */
	public static String getUploadPathName(File pathFile){
		String createFileName=null;
		String uploadPathName=null;
		//windows环境下
//		if(pathFileName.lastIndexOf('\\')>0){
//			int s = pathFileName.lastIndexOf('\\');
//			System.out.println("s="+s);
//			createFileName=pathFileName.substring((s+1 ),pathFileName.length());
//		}
//		if(pathFileName.lastIndexOf('/')>0){
//			int e = pathFileName.lastIndexOf('/');
//			System.out.println("e="+e);
//			createFileName=pathFileName.substring((e+1),pathFileName.length());
//		}
		if(pathFile.exists()){
			
				createFileName=pathFile.getName()+".back";
		
				String uploadLocalPath = PropUtil.getProp("UPLOAD_PATH", "/config/aibi_pop/ftp.properties");
				File uploadDir=new File(uploadLocalPath);
				if(!uploadDir.exists()){
					uploadDir.mkdir();
				}
				uploadPathName=uploadLocalPath+"/"+createFileName;
//				System.out.println("上传文件绝对路径"+uploadPathName);
				
			
		}
		return uploadPathName;
	}
	
	/**
	 * 销户同步数据成功生成文件
	 */
	public static boolean  createFile(File pathFile,String uploadPathName,boolean isSuccess){
			boolean flag=true;
		if(!pathFile.exists()||"".equals(uploadPathName)){
			return false;
		}
		if(uploadPathName!=null){
				File file;
				InputStream is=null;
				Reader rd=null;
				BufferedReader br=null;
				
				OutputStream os=null;
				Writer ow=null;
				PrintWriter pw=null;
				try {
				
					is=new FileInputStream(pathFile);
					rd=new InputStreamReader(is,"utf-8");
					br=new BufferedReader(rd);
				
					File f = new File(uploadPathName);
					if(!f.exists()){
						
						f.createNewFile();
					}
					os=new FileOutputStream(f);
					ow=new OutputStreamWriter(os,"utf-8");
					pw=new PrintWriter(ow);
					String oldLine=br.readLine();
					if(oldLine!=null && !"".equals(oldLine)){
						if(isSuccess){
							String backLine=oldLine+",1";
							pw.write(backLine);
						}else{
							String backLine=oldLine+",0";
							pw.write(backLine);
						}
						
					}else{
						flag=false;
						return flag;
					}
				} catch (Exception e) {
					e.printStackTrace();
					flag=false;
					return flag;
				}finally{
					if(br!=null){
						try {
							br.close();
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					if(pw!=null){
						pw.close();
					}
				}
		
		}else{
			flag=false;
			return flag;
		}
		return flag;
	}
	/**
	 * 根据本地上传路径+文件名获取远程文件名
	 */
	public static String getRemoteFileName(String uploadPathName){
		
		String remoteFileName=null;
		if(uploadPathName!=null){
		
			if(uploadPathName.lastIndexOf('/')>0){
				int e = uploadPathName.lastIndexOf('/');
		//		System.out.println("e="+e);
				remoteFileName=uploadPathName.substring((e+1),uploadPathName.length());
				
			}
		}
		return remoteFileName;
	}
	/**
	 * 创建上传文件后上传文件,pathFileName代表下载到本地的文件路径+文件名
	 * remoteFileName，指上传到boss的远程文件名字
	 * uploadRemotePath，指上传到boss的远程路径+名字
	 */
	public static boolean upload2BOSS(File pathFile,boolean isSuccess){
		String uploadPathName = getUploadPathName(pathFile);
		String remoteFileName = getRemoteFileName(uploadPathName);
		boolean createIsOK = createFile(pathFile,uploadPathName,isSuccess);
		return createIsOK;
//		if(createIsOK){
//			ApacheFtpUtil apacheFtp = null;
//			try {
//				String ip=PropUtil.getProp("FTP_ADDRESS", "/config/aibi_pop/ftp.properties");
//				String port=PropUtil.getProp("FTP_PORT", "/config/aibi_pop/ftp.properties");
//				String user=PropUtil.getProp("FTP_USER", "/config/aibi_pop/ftp.properties");
//				String pwd=PropUtil.getProp("FTP_PASSWORD", "/config/aibi_pop/ftp.properties");
//				String encode=PropUtil.getProp("FTP_ENCODE", "/config/aibi_pop/ftp.properties");
//				String uploadPath=PropUtil.getProp("UPLOAD_PATH", "/config/aibi_pop/ftp.properties");
//				log.debug("登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ip,port, user,pwd, encode);
//				
//				apacheFtp = ApacheFtpUtil.getInstance(ip,Integer.parseInt(port),user,pwd, encode );
//				String uploadRemotePath =uploadPath;
//				apacheFtp.changeDir(uploadRemotePath);
//				InputStream is=new FileInputStream(uploadPathName );
//				apacheFtp.upload2BOSS(is, remoteFileName, FTP.BINARY_FILE_TYPE);
//			} catch (Exception e) {
//				log.error("", e);
//				return false;
//			} finally {
//				if (apacheFtp != null) {
//					apacheFtp.forceCloseConnection();
//				}
//		
//			}
//			return true;
//		}else{
//			return false;
//		}
	}
	//开户
	
	public static String getUploadPathName2(File pathFile,String ftpType){
		String createFileName="";
		String uploadPathName="";
		
		if(pathFile.exists()){
			
				createFileName=pathFile.getName()+".back";
				FtpConfig	ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(ftpType);
				String uploadLocalPath = ftpConfig.getUPLOAD_LOCAL_PATH();
				File uploadDir=new File(uploadLocalPath);
				if(!uploadDir.exists()){
					uploadDir.mkdir();
				}
				uploadPathName=uploadLocalPath+"/"+createFileName;
				System.out.println("开户上传文件绝对路径"+uploadPathName);
				
			
		}
		return uploadPathName;
	}
	

	
	/**
	 * 创建上传文件后上传文件,pathFileName代表下载到本地的文件路径+文件名
	 * remoteFileName，指上传到boss的远程文件名字
	 * uploadRemotePath，指上传到boss的远程路径+名字
	 */
	public static boolean upload2BOSSForInstall(File pathFile,boolean isSuccess,String ftpType){
		String uploadPathName = getUploadPathName2(pathFile,ftpType);
		String remoteFileName = getRemoteFileName(uploadPathName);
		boolean createIsOK = createFile(pathFile,uploadPathName,isSuccess);
		return createIsOK;
//		if(createIsOK){
//			FtpConfig	ftpConfig = FtpConfigure.getInstance().getFtpConfigByTypes(ftpType);
//			ApacheFtpUtil apacheFtp = null;
//			try {
//				log.debug("登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}",ftpConfig.getFTP_ADDRESS(),ftpConfig.getFTP_PORT(), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
//			//	System.out.println("登陆ftp服务器:ip={}, port={},user={},passwd={},encode={}"+ftpConfig.getFTP_ADDRESS()+ftpConfig.getFTP_PORT()+ ftpConfig.getFTP_USER()+ftpConfig.getFTP_PASSWORD()+ftpConfig.getFTP_ENCODE());
//				apacheFtp = ApacheFtpUtil.getInstance(ftpConfig.getFTP_ADDRESS(),Integer.parseInt(ftpConfig.getFTP_PORT()), ftpConfig.getFTP_USER(),ftpConfig.getFTP_PASSWORD(), ftpConfig.getFTP_ENCODE());
//				String uploadRemotePath = ftpConfig.getLOCAL_PATH();
//				apacheFtp.changeDir(uploadRemotePath);
//				InputStream is=new FileInputStream(uploadPathName );
//				apacheFtp.upload2BOSS(is, remoteFileName, FTP.BINARY_FILE_TYPE);
//			} catch (Exception e) {
//				log.error("", e);
//				return false;
//			} finally {
//				if (apacheFtp != null) {
//					apacheFtp.forceCloseConnection();
//				}
//		
//			}
//			return true;
//		}else{
//			return false;
//		}
	}
	
	
	
}
