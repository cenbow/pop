package com.ai.bdx.pop.task.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.pop.util.ftp.PropUtil;
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void readFileByLine() {
		Stopwatch timeWatch = Stopwatch.createStarted();
		try {
			Files.readLines(file, Charset.forName(charsetName),
					new LineProcessor() {
						@Override
						public boolean processLine(String line)throws IOException {
							String[]  arrs=line.split(",");
							//待处理处理行字符分割
							
							String ruleId="";
						    String taskId="";
							try {
								FeedBackStatusCacheQueue.put(ruleId,taskId,arrs);
							} catch (InterruptedException e) {
								log.error("put FeedBackStatusCacheQueue error,{}",e);
							}
							return true;
						}
						@Override
						public List<String[]> getResult() {
							return null;
						}
					});

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.info("文件读取完毕{},共计" + timeWatch.elapsed(TimeUnit.SECONDS) + "秒",file.getName());
			if (deleteFile) {
				file.delete(); // 临时文件删掉
			}
		}
	}

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
	 * 
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
		
				String uploadLocalPath = PropUtil.getProp("UPLOAD_PATH", "/config/aibi_pop/popftp.properties");
				File uploadDir=new File(uploadLocalPath);
				if(!uploadDir.exists()){
					uploadDir.mkdir();
				}
				uploadPathName=uploadLocalPath+"/"+createFileName;
				System.out.println("上传文件绝对路径"+uploadPathName);
				
			
		}
		return uploadPathName;
	}
	
	/**
	 * 
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
		System.out.println("宽带变更上传文件前生成back回馈文件："+createIsOK);
		return createIsOK;

	}
}
