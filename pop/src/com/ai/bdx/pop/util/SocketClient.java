package com.ai.bdx.pop.util;

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

import com.asiainfo.biframe.utils.config.Configure;
  
/**
 * socketUtil 
 * @author luozn
 *
 */
  
public class SocketClient {  
  
    public static final Object locked = new Object();  
    public static final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(  
            1024 * 100);  
  
    class SendThread extends Thread{  
        private Socket socket;  
        public SendThread(Socket socket) {  
            this.socket = socket;  
        }  
        public void run(String ruleId) {  
            while(true){  
                try {
                	String filePath= Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
                	File f=new File(filePath+ruleId+".txt");
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String str="";
                    while((str=bf.readLine())!=null)
                    {
                    	if(str.trim().length()>0){	
	                    Thread.sleep(1000); 
	                    String[] s=str.split(",");
	                    for(int i=0;i<s.length;i++){
	                    	PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));  
	                    	pw.write(s[i]);  
	                    	pw.flush();
	                    	}
                        }
                   }
                } catch (Exception e) {  
                    e.printStackTrace();  
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
                try {                     
                    Reader reader = new InputStreamReader(socket.getInputStream());  
                    CharBuffer charBuffer = CharBuffer.allocate(8192);  
                    int index = -1;  
                    while((index=reader.read(charBuffer))!=-1){  
                        charBuffer.flip();  
                        System.out.println("client:"+charBuffer.toString());  
                    }  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
      
    public void start(String ruleId ) throws UnknownHostException, IOException{
    	String hostIp=Configure.getInstance().getProperty("ZTEHOSTIP");
    	Integer hostPort=Integer.parseInt(Configure.getInstance().getProperty("ZTEHOSTPORT"));
        Socket socket = new Socket(hostIp,hostPort);  
        new SendThread(socket).start();  
        new SendThread(socket).run(ruleId);
        new ReceiveThread(socket).start();  
    }  
    public static void main(String[] args) throws UnknownHostException, IOException {  
         
    }  
}
	    
	    
	    
	    
	  

