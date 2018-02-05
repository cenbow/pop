package com.ai.bdx.pop.wsclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
  
/**
 * socketUtil 
 * @author luozn
 *
 */
  
public class SocketClient {  
  
    public static final Object locked = new Object();  
    public static final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(  
            1024 * 100);  
    private static Logger log = Logger.getLogger(SocketClient.class.getName());
  
    class SendThread extends Thread{  
        private Socket socket;  
        private String filePath;
        public SendThread(Socket socket,String filePath) {  
            this.socket = socket;  
            this.filePath = filePath;
        }  
        public void run() {  
            while(true){  
            	BufferedReader bf = null;
                try {
                	//String filePath= Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
//                	String filePath = "";  //后期传过来
                	File f=new File(filePath+".txt");
                    bf = new BufferedReader(new FileReader(f));
                    String str="";
                    while((str=bf.readLine())!=null)
                    {
                    	if(str.trim().length()>0){	
	                    Thread.sleep(1000); 
	                    String[] s=str.split(";");
	                    for(int i=0;i<s.length;i++){
	                    	PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));  
//	                    	PrintStream ps = new PrintStream(socket.getOutputStream());
//	                    	ps.write(s[i].getBytes());
	                    	pw.write(s[i]);  
	                    	pw.flush();
	                    	pw.close();
	                    	}
                        }
                   }
                   bf.close();
                } catch (Exception e) {  
                    e.printStackTrace();  
                }
                finally{
                	if(bf!=null){
                		try {
                			bf.close();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
                	}
                }
            }  
        }  
       
    }  
  
    class ReceiveThread extends Thread{  
        private Socket socket;  
          
        public ReceiveThread(Socket socket) {  
            this.socket = socket;  
        }  
  
        @Override  
        public void run() {  
            while(true){  
            	Reader reader = null;
                try {
                    reader = new InputStreamReader(socket.getInputStream());  
                    CharBuffer charBuffer = CharBuffer.allocate(8192);  
                    int index = -1;  
                    while((index=reader.read(charBuffer))!=-1){  
                        charBuffer.flip();  
                        log.info("client:"+charBuffer.toString());  
                    }
                    reader.close();
                } catch (Exception e) {  
                    e.printStackTrace();  
                }
                finally {
        			if (reader != null) {
        				try {
        					reader.close();
        				} catch (IOException e1) {
        					e1.printStackTrace();
        				}
        			}
        		}
            }  
        }  
    }  
      
    public void start(String filePath) throws UnknownHostException, IOException{
    	//String hostIp=Configure.getInstance().getProperty("ZTEHOSTIP");
    	//Integer hostPort=Integer.parseInt(Configure.getInstance().getProperty("ZTEHOSTPORT"));
    	String hostIp = PropUtil
		.getProp("zte_host_ip", "pcrf.properties");     //中兴socketIP地址
    	Integer hostPort = Integer.valueOf(PropUtil
    			.getProp("zte_host_port", "pcrf.properties"));   //中兴socket端口
        Socket socket = new Socket(hostIp,hostPort);  
        new SendThread(socket,filePath).start(); 
//        new SendThread(socket).run(filePath);
        new ReceiveThread(socket).start();  
        if(socket!=null){
        	socket.close();        	
        }
    }  
    public static void main(String[] args) throws UnknownHostException, IOException {  
         
    }  
}
	    
	    
	    
	    
	  

