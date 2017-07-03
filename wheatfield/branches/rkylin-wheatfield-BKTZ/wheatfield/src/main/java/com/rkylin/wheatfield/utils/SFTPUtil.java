package com.rkylin.wheatfield.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
/**
 * 使用SFTP上传下载文件
 * @author lzh
 *
 */

@Component("sftpUtil")
public class SFTPUtil {
	private static final Logger logger = LoggerFactory.getLogger(SFTPUtil.class);

	private String host = "10.19.0.216";
	private String username = "haike";
	private String password = "hk@cj253";
	private int port = 22;
	private ChannelSftp sftp = null;
	
	public SFTPUtil(){
		
	}
	public SFTPUtil(String ip,int port,String username,String password){
		this.host = ip;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ChannelSftp getSftp() {
		return sftp;
	}

	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}

	/**
	 * connect server via sftp
	 */
	public int connect() {
		try {
			if (sftp != null) {
				System.out.println("sftp is not null");
			}
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			logger.debug("SFTP will connect to "+host+":"+port);
			Session sshSession = jsch.getSession(username, host, port);
			logger.debug("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			logger.debug("Connecting session...");
			sshSession.connect();
			logger.debug("Session connected.");
			logger.debug("Opening Channel...");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			logger.debug("Channel opened.");
			sftp = (ChannelSftp) channel;
			logger.debug("Connected to " + host+":"+port + ".");
		} catch (Exception e) {
			logger.error("Failed to connect to "+host+":"+port,e);
			return 0;
		}
		return 1;
	}

	/**
	 * Disconnect with server
	 */
	public void disconnect() {
		if (this.sftp != null) {
			if (this.sftp.isConnected()) {
				this.sftp.disconnect();
				logger.warn("sftp is closed successfully");
			} else if (this.sftp.isClosed()) {
				logger.warn("sftp is closed already");
			}
		}

	}

	/**
	 * 
	 * @param remotePath
	 *            远程目录
	 * @param remoteFile
	 *            保存在远程时取的名字
	 * @param localFile
	 *            本地文件带全路径
	 * @param sftp
	 *            已经建立连接的 sftp对象
	 * @throws SftpException
	 *             有可能会出现 指定的路径不存在的异常 错误码 2 No such file
	 * @throws FileNotFoundException
	 *             本地文件不存在
	 */
	public void download(String remotePath, String remoteFile, String localFile)
			throws SftpException, FileNotFoundException {
		sftp.cd(remotePath);
		sftp.get(remoteFile, new FileOutputStream(new File(localFile)));
	}

	/**
	 * 
	 * @param remotePath
	 *            远程目录
	 * @param remoteFile
	 *            保存在远程时取的名字
	 * @param localFile
	 *            本地文件带全路径
	 * @param sftp
	 *            已经建立连接的 sftp对象
	 * @throws SftpException
	 *             有可能会出现 指定的路径不存在的异常 错误码 2 No such file
	 * @throws FileNotFoundException
	 *             本地文件不存在
	 */
	public void upload(String remotePath, String remoteFile, String localFile)
			throws SftpException, FileNotFoundException {
		sftp.cd(remotePath);
		File f = new File(localFile);
		if(f.exists()) {
			sftp.put(new FileInputStream(new File(localFile)), remoteFile);
		}

	}

	public static void main(String[] args) throws FileNotFoundException,
			SftpException, InterruptedException {
		SFTPUtil ftp = new SFTPUtil();
		ftp.setHost("115.238.110.126");
		ftp.setPort(2122);
		ftp.setUsername("dlrsjf");
		ftp.setPassword("dlrsjf@123");
		
		ftp.connect();
//		ftp.upload("/HAIKE_duizhang", "48330000201303161.xls",
//				"d:\\temp\\a.xls");
		ftp.download("/dlrsjf/201508131000455502/", "JYMX_201508131000455502_20151101.txt", "c:\\test\\download\\JYMX_201508131000455502_20151101.txt");
		Thread.sleep(2000);
		ftp.disconnect();
		System.exit(0);
	}

}