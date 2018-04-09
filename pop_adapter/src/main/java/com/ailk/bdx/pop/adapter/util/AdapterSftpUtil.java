package com.ailk.bdx.pop.adapter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ailk.bdx.pop.adapter.util.sftp.FileProgressMonitor;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
/**
   * @ClassName: ApacheSftpUtil 
   * @Description: SFTP客户端工具类
   * @author zhilj
   * @date 创建时间：2015-7-9
 */
public class AdapterSftpUtil {
	
	private final ChannelSftp channelSftp;
	
	private Session session = null;
	
	private static final Logger log = LogManager.getLogger();
	
	private static final int SFTP_DEFAULT_PORT = 22;
	/**
	 * 构造函数，传递连接参数
	 * @param server
	 * @param port
	 * @param loginName
	 * @param loginPwd
	 * @param timeout
	 * @throws JSchException
	 */
	public AdapterSftpUtil(String server, String port, String loginName, String loginPwd,int timeout) throws JSchException {
		//主机IP
		String ftpHost = server;
		//用户名密码
		String ftpUserName = loginName;
		String ftpPassword = loginPwd;
		//端口号
		int ftpPort = SFTP_DEFAULT_PORT;
		
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}
		
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		log.debug("Session created.");
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		session.setConfig("StrictHostKeyChecking", "no");// 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		log.debug("Session connected.");

		log.debug("Opening Channel.");
		channelSftp = (ChannelSftp) session.openChannel("sftp"); // 打开SFTP通道
		channelSftp.connect(); // 建立SFTP通道的连接
		log.debug("Connected successfully to ftpHost = {},as ftpUserName = {}", new Object[] { ftpHost, ftpUserName });
	}
	
	public Vector<String> listFileInDir(String remoteDir) throws Exception {
		try {
			Vector<LsEntry> rs = channelSftp.ls(remoteDir);
			Vector<String> result = new Vector<String>();
			for (int i = 0; i < rs.size(); i++) {
				if (!rs.get(i).getAttrs().isDir()) {
					result.add(rs.get(i).getFilename());
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(remoteDir);
			throw new Exception(e);
		}
	}
	
	public Vector<String> listFileInDirByFormat(String remoteDir,String fromatStr) throws Exception {		
		try {
			final Pattern pattern = Pattern.compile(fromatStr);
			final Vector<String> result = new Vector<String>();
			LsEntrySelector selector = new LsEntrySelector() {				
				public int select(LsEntry entry) {
					Matcher mtc = pattern.matcher(entry.getFilename());
					SftpATTRS attr = entry.getAttrs();
					if (mtc.find() && !attr.isDir() && !attr.isLink()) {
						result.add(entry.getFilename());
					}
					return CONTINUE;
				}
			};
			channelSftp.ls(remoteDir, selector);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(remoteDir);
			throw new Exception(e);
		}
	}

	public Vector<String> listSubDirInDir(String remoteDir) throws Exception {
		Vector<LsEntry> rs = channelSftp.ls(remoteDir);
		Vector<String> result = new Vector<String>();
		for (int i = 0; i < rs.size(); i++) {
			if (isARemoteDirectory(rs.get(i).getFilename())) {
				result.add(rs.get(i).getFilename());
			}
		}
		return result;
	}

	protected boolean createDirectory(String dirName) {
		try {
			channelSftp.mkdir(dirName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean changeDir(String remotePath) {
		try {
			channelSftp.cd(remotePath);
		} catch (SftpException e) {
			return false;
		}
		return true;
	}

	public boolean isARemoteDirectory(String path) {
		try {
			return channelSftp.stat(path).isDir();
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getWorkingDirectory() throws SftpException {
		return channelSftp.pwd();
	}

	/**
	 * 变更工作目录(不存在则创建)
	 *
	 * @param remoteDir--目录路径
	 */
	public void changeAndMakeDir(String remoteDirectory) {
		if (!changeDir(remoteDirectory)) {
			createDirectory(remoteDirectory);
			changeDir(remoteDirectory);
		}
	}

	/**
	 * 下载文件
	 * @param remotePath 远程文件路径
	 * @param localPath 本地文件路径(如果只是文件夹，文件名与源文件一致)
	 * @return
	 * @throws IOException
	 * @throws SftpException 
	 */
	public boolean downloadFile(String remotePath, String localPath) throws IOException, SftpException {
		try {
			channelSftp.get(remotePath, localPath, new FileProgressMonitor(new File(remotePath).length()),
					ChannelSftp.OVERWRITE);
		} catch (SftpException e) {
			System.err.println(remotePath + " not found in " + channelSftp.pwd());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 下载文件
	 * @param remotePath 远程文件路径
	 * @param localPath 本地文件路径(必须是带文件的路径)
	 * @return
	 * @throws IOException
	 * @throws SftpException 
	 */
	public boolean downloadFileAfterCheck(String remotePath, String localPath) throws IOException, SftpException {
		FileOutputStream outputSrr = null;
		try {
			File file = new File(localPath);
			if (!file.exists()) {
				outputSrr = new FileOutputStream(localPath);
				channelSftp.get(remotePath, outputSrr);
			}
		} catch (SftpException e) {
			System.err.println(remotePath + " not found in " + channelSftp.pwd());
			e.printStackTrace();
			return false;
		} finally {
			if (outputSrr != null) {
				outputSrr.close();
			}
		}
		return true;
	}

	/**
	 * 上传文件
	 * @param localPath 本地文件路径
	 * @param remotePath 远程文件路径（如果只是文件夹，文件名与源文件一致）
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(String localPath, String remotePath) throws IOException {
		try {
			channelSftp.put(localPath, remotePath, new FileProgressMonitor(new File(localPath).length()),
					ChannelSftp.OVERWRITE);
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 上传文件
	 * @param localFile
	 * @param remotePath 远程文件路径（必须是带文件的路径）
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(File localFile, String remotePath) throws IOException {
		try {
			channelSftp.put(new FileInputStream(localFile), remotePath, new FileProgressMonitor(localFile.length()),
					ChannelSftp.OVERWRITE);
		} catch (SftpException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 关闭连接
	 * @throws Exception
	 */
	public void closeChannel() throws Exception {
		if (channelSftp != null) {
			channelSftp.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String src = "D:\\workspace\\TestMap\\"; // 本地文件名
		String dst = "/home/asiainfo/wuhc1/A94009CS20140411_174000.CHK"; // 目标文件名

		AdapterSftpUtil sftpInstance = new AdapterSftpUtil("10.19.115.146","22","","",30000);
		long startTime = System.currentTimeMillis();
		sftpInstance.downloadFileAfterCheck(dst, src);
		log.debug("耗时秒:" + TimeUnit.SECONDS.convert(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS));
		sftpInstance.closeChannel();

	}
}
