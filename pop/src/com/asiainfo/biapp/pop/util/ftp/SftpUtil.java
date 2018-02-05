package com.asiainfo.biapp.pop.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ai.bdx.pop.wsclient.bean.SprBusinessBean;
import com.ai.bdx.pop.wsclient.util.StringUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpUtil {
		private static Logger log = Logger.getLogger(SftpUtil.class.getName());

//	  private static String hostname = "10.31.101.160";
	  private static int sftpPort = Integer.valueOf(PropUtil.getProp("sftpServerPort",
		"ftp.properties"));

//	  private static String sftpUsername = "ocdc";
//	  private static String sftpPassword = "ocdc%%";
//
//	  private static String remoteDirectoryString = "/home/ocdc/sftptest";
	  private static String localDirectoryString = PropUtil.getProp("sftpLocalPath",
		"ftp.properties");

	  /**
	   * @Description: Upload
	   */
	  public static void upload(File localFile,SprBusinessBean sprBean) {
	    File localDirectory = new File(localDirectoryString);

	    if (localDirectory.exists()) {
	      ChannelSftp sftp = connect(sprBean.getSprIp(), sftpPort, sprBean.getFtpUsr(), sprBean.getFtpPwd());

//	      Collection<File> localFileCollection = FileUtils.listFiles(localDirectory, new String[] { "gz", "GZ", "xml", "XML", "zip", "ZIP" }, true);
//	      Iterator<File> localFileIterator = localFileCollection.iterator();

//	      while (localFileIterator.hasNext()) {
//	        File localFile = localFileIterator.next();
//	        log.info("Local File: " + localFile);
//
//
//	      }
	      
	        if (sftp != null && sftp.isConnected()) {
		          upload(sprBean.getFtpAddress(), localFile, sftp);
		
		    }

	      disconnect(sftp);
	    } else {
	      log.info("The Local Directory " + localDirectoryString + " doesn't exist");
	    }
	  }

	  /**
	   * @Description: SFTP Upload
	   * 
	   * @param remoteDirectoryString
	   *            Remote CM Directory
	   * @param localFile
	   *            Local CM File
	   * @param sftp
	   *            ChannelSftp
	   */
	  public static void upload(String remoteDirectoryString, File localFile, ChannelSftp sftp) {
	    log.info("Remote Directory: " + remoteDirectoryString);
	    String deleteLocal = PropUtil.getProp("deleteLocal", "ftp.properties");
	    try {
	      sftp.cd(remoteDirectoryString);
	      FileInputStream fis = new FileInputStream(localFile);
	      sftp.put(fis, localFile.getName());

	      if(fis != null){
	    	  try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	      }
	   // 是否需要删除本地文件
	      if (!StringUtil.isEmpty(deleteLocal) && "1".equals(deleteLocal)) {
		      if(!localFile.delete()){
					log.info("请关闭使用该文件的所有进程或者流！！");
		      }else{
					log.info("文件删除成功！");
		      }
			}

	      log.info("Upload Local File " + localFile + " via SFTP");
	    } catch (FileNotFoundException fnfe) {
	    	fnfe.printStackTrace();
	    } catch (SftpException se) {
	    	se.printStackTrace();
	    }
	  }

	  /**
	   * @Description: SFTP Connection
	   * 
	   * @param hostname
	   *            SFTP HOST
	   * @param sftpPort
	   *            SFTP PORT
	   * @param sftpUsername
	   *            SFTP USERNAME
	   * @param sftpPassword
	   *            SFTP PASSWORD
	   * 
	   * @return ChannelSftp
	   */
	  public static ChannelSftp connect(String hostname, int sftpPort, String sftpUsername, String sftpPassword) {
	    JSch jsch = new JSch();
	    Properties sshConfig = new Properties();

	    Channel channel = null;

	    try {
	      Session session = jsch.getSession(sftpUsername, hostname, sftpPort);
	      log.info("Session Created");

	      session.setPassword(sftpPassword);

	      sshConfig.put("StrictHostKeyChecking", "no");
	      session.setConfig(sshConfig);
	      // session.setTimeout(60000);
	      // session.setServerAliveInterval(90000);

	      session.connect();
	      log.info("Session Connected");

	      channel = session.openChannel("sftp");
	      log.info("Channel Opened");

	      channel.connect();
	      log.info("Channel Connected");
	    } catch (JSchException je) {
	      je.printStackTrace();
	    }

	    return (ChannelSftp) channel;
	  }

	  /**
	   * @Description: Disconnect SFTP
	   * 
	   * @param sftp
	   *            ChannelSftp
	   */
	  public static void disconnect(ChannelSftp sftp) {
	    if (sftp != null) {
	      try {
	        sftp.getSession().disconnect();
	      } catch (JSchException je) {
	        je.printStackTrace();
	      }

	      sftp.disconnect();
	    }
	  }
	  
	  public static void main(String[] args) {
		  File localFile = new File("D:\\localpath\\BatFile_Service_20150803111004_01.txt");
		  SprBusinessBean sprBean = new SprBusinessBean();
		  sprBean.setFtpAddress("/home/ocdc/sftptest");
		  sprBean.setSprIp("10.31.101.160");
		  sprBean.setFtpUsr("ocdc");
		  sprBean.setFtpPwd("ocdc%%");
		  SftpUtil.upload(localFile,sprBean);
	}
}
