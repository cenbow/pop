package com.ai.bdx.pop.controller;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import jodd.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.model.PopPolicyBaseinfoFiles;
import com.ai.bdx.pop.util.PopUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class FileManagerController extends PopController{

	private static final Logger log = LogManager.getLogger(FileManagerController.class);
	public void forwordUploadFiels(){
		initAttributes();
		String id=this.getPara("id");
		log.debug("策略{}准备上传文件",id);
		/*HashMap<String, Object> maps=new HashMap<String, Object>();
		if(StringUtil.isNotBlank(id)){
			maps.put("policy_id", id);
		}
		List<PopPolicyBaseinfoFiles> files = PopPolicyBaseinfoFiles.dao().search(maps);
		this.setAttr("selectFiles", files);
		*/
		setAttr("plocyid", id);
		render("policyConfig/policySceneUpload.jsp");
	}
	
	public void upload() throws IOException {
		this.getRequest().setCharacterEncoding("UTF-8");
		this.getResponse().setContentType("text/html; charset=UTF-8");
		PrintWriter out = getResponse().getWriter();

		// 上传文件的保存路径
		String configPath = "jiaoben";
		String dirTemp = "jiaoben/temp";
		String filePath = Configure.getInstance().getProperty("SYS_COMMON_UPLOAD_PATH");
		// 文件保存目录路径
		String savePath = configPath;
		// 临时文件目录
		String tempPath = dirTemp;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new java.util.Date());
		savePath += File.separator + ymd + File.separator;
		// 创建文件夹
		File dirFile = new File(filePath+savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		tempPath += File.separator + ymd + File.separator;
		// 创建临时文件夹
		File dirTempFile = new File(filePath+tempPath);
		if (!dirTempFile.exists()) {
			dirTempFile.mkdirs();
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(20 * 1024 * 1024); // 设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
		factory.setRepository(new File(filePath+tempPath)); // 设定存储临时文件的目录。

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		try {
			List<?> items = upload.parseRequest(this.getRequest());
			Iterator<?> itr = items.iterator();

			String plocyid = "";
			String manufacturers = "";
            String FName="";
            String path="";
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				String fileName = item.getName();
				long fileSize = item.getSize();
				if (!item.isFormField()) {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String newFileName = df.format(new java.util.Date()) + "_"+ new Random().nextInt(1000) + "." + fileExt;
					try {
						path=savePath+newFileName;
						File uploadedFile = new File(filePath+savePath, newFileName);
						OutputStream os = new FileOutputStream(uploadedFile);
						InputStream is = item.getInputStream();
						byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
						int length = 0;
						while ((length = is.read(buf)) > 0) {
							os.write(buf, 0, length);
						}
						// 关闭流
						os.flush();
						os.close();
						is.close();
						log.debug("上传成功！路径：" + filePath+savePath + "/" + newFileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					String filedName = item.getFieldName();
					if ("manufacturers".equals(filedName)) {
						manufacturers = item.getString();
					} else if ("plocyid".equals(filedName)) {
						plocyid = item.getString();
					}else if("Filename".equals(filedName)){
						FName=item.getString("UTF-8");
					}
				}
			}
			
		String id=this.saveUploadFile(FName, plocyid, "", path, manufacturers);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.flush();
		out.close();

		// this.setAttr("id", id);
		this.renderJson();
	}
	private String saveUploadFile(final String fileName,final String policyID,final String ruleID,final String filePath,final String manufacturers) {
		final String id =PopUtil.generateId();
		initAttributes();
		boolean succeed = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {

				PopPolicyBaseinfoFiles file = new PopPolicyBaseinfoFiles();
				file.set(PopPolicyBaseinfoFiles.COL_ID, id);
				file.set(PopPolicyBaseinfoFiles.COL_POLICY_ID, policyID);
				file.set(PopPolicyBaseinfoFiles.COL_RULE_ID, ruleID);
				file.set(PopPolicyBaseinfoFiles.COL_FILENAME, fileName);
				file.set(PopPolicyBaseinfoFiles.COL_FILEPATH, filePath);
				file.set(PopPolicyBaseinfoFiles.COL_MANUFACTURERS, manufacturers);
				file.set(PopPolicyBaseinfoFiles.COL_CREATEUSERID, userId);
				file.set(PopPolicyBaseinfoFiles.COL_CREATE_DATE, new java.util.Date());
				file.save();
				return true;
			}
		});
		if (succeed) {
			return id;
		} else {
			return null;
		}
	}
	public void uploadSuccess() {
		String policyid=this.getPara("id");
		String manufacturers=this.getPara("manufacturers");
		HashMap<String, Object> maps=new HashMap<String, Object>();
		if(StringUtil.isNotBlank(policyid)){
			maps.put("policy_id", policyid);
		}
		if(StringUtil.isNotBlank(manufacturers)){
			maps.put("manufacturers", manufacturers);	
		}
		List<PopPolicyBaseinfoFiles> files = PopPolicyBaseinfoFiles.dao().search(maps);
		
		this.setAttr("selectFiles", files);
		this.renderJsp("policyConfig/FileImportResult.jsp");
	}
	
	
	public void forwordDownLoadFiels(){
		String policyid=this.getPara("id");
		HashMap<String, Object> maps=new HashMap<String, Object>();
		if(StringUtil.isNotBlank(policyid)){
			maps.put("policy_id", policyid);
		}
		List<PopPolicyBaseinfoFiles> files = PopPolicyBaseinfoFiles.dao().search(maps);
		this.setAttr("selectFiles", files);
		this.setAttr("flag", 1);
		
		this.renderJsp("policyConfig/FileImportResult.jsp");
	}
}
