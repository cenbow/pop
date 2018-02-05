package com.ai.bdx.pop.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.ai.bdx.pop.base.PopController;
import com.ai.bdx.pop.bean.BatchStationBean;
import com.ai.bdx.pop.bean.ImportBatchCutoverBean;
import com.ai.bdx.pop.model.PopPolicyBaseinfoFiles;
import com.ai.bdx.pop.service.BatchStationService;
import com.ai.bdx.pop.service.impl.BatchStationServiceImpl;
import com.ai.bdx.pop.util.PopUtil;
import com.ai.bdx.pop.util.SpringContext;
import com.ai.bdx.pop.util.excel.BatchStationUtil;
import com.ai.bdx.pop.util.excel.ExcelUtil2007;
import com.asiainfo.biapp.pop.Exception.MysqlDataAccessExcetion;
import com.asiainfo.biapp.pop.util.ftp.PropUtil;
import com.asiainfo.biframe.utils.config.Configure;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.upload.UploadFile;

/**
 * 基站批量割接
 * @author 林
 *
 */
public class BatchStationCutoverController extends PopController{
	private static final Logger log = LogManager.getLogger(BatchStationCutoverController.class);

	/**
	 * 将前台页面传过来的批量基站信息转换成bean
	 * 参数ids为前台页面勾选的每一条对应的subsid,product,loction
	 * @return 返回值为每一条对应的bean对象
	 */
	public List<BatchStationBean> getBatchStation2Bean(){
		List<BatchStationBean> list =new ArrayList<BatchStationBean>();
		String ids = this.getPara("ids");
		String[] sp = ids.split(":");
		System.out.println("参数重置上传参数ids="+ids);
		
		if(ids!=null && !"".equals(ids)){
			log.info("开始基站批量割接.......");
			BatchStationBean stationBean = null;
			try{
		
				for(int i=0;i<sp.length;i++){
					stationBean=new BatchStationBean();
					String[] id = sp[i].split("[+]");
					stationBean.setSubsid(id[0]);
					stationBean.setProductNo(id[1]);
					stationBean.setUserlocationOld(id[2]);
					stationBean.setUserlocationNew(id[3]);
					stationBean.setLac_ci_hex_code_old(id[4]);
					stationBean.setLac_ci_hex_code_new(id[5]);
					list.add(stationBean);
//					System.out.println("勾选的bean"+stationBean.toString());
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	
	return list;
	}
	
	
	
	/**
	 * 调用PCC接口入口
	 */
	@SuppressWarnings("unchecked")
	public void batchStationCutover(){
		BatchStationService batchStationService = null;
		Map<String, String> result  = new HashMap<String,String>();
		System.out.println("基站割接开始------");
		try {
			batchStationService=SpringContext.getBean("batchStationService",BatchStationService.class);
			List<BatchStationBean> beanList = getBatchStation2Bean();
			ArrayList<BatchStationBean> beanList2=(ArrayList<BatchStationBean>)beanList;
			//克隆beanlist集合
			ArrayList<BatchStationBean> beanListCopy = (ArrayList<BatchStationBean>)beanList2.clone();
			//将克隆后的集合传给batchStation2List方法
			List<Map<String,Object>> mobileList = batchStationService.batchStation2List(beanListCopy);
			//将原始的beanlist和根据beanlist转成的mobilelist传给service
			Map<String, Object> resultsMap = batchStationService.cpeBsBatchcg(beanList,mobileList);
			//"1"表示调用pcc接口成功，success表示成功的手机集合
			List<String> success=(List<String>) resultsMap.get("1");
			//根据成功的手机集合过滤原始的beanlist，返回所有成功手机号的beanlist
			List<Map<String,Object>> mobileListForSuccess=batchStationService.getMobileListForSuccess(mobileList, success);
			//对成功的手beanlist更新redis信息
			batchStationService.updateRedisInfoForSuccess(mobileListForSuccess);
			if(success.size()>0){
				result.put("msg", "参数重置成功");
			}else{
				result.put("msg", "参数重置失败");
			}
		}catch (MysqlDataAccessExcetion e) {
			result.put("msg", "操作失败");
			log.error("基站割接更新CPE锁网信息失败:Mysql数据库访问异常");
			e.printStackTrace();
		} catch (Exception e) {
			result.put("msg", "操作失败");
			log.error("基站割接更新CPE锁网信息失败:"+e.getMessage());
			e.printStackTrace();
		}
		log.info("基站割接结束.........");
		
	
		this.renderJson(result);
	}
	
	/**
	 * 批量参数重置controller查询
	 */
	public void getBatchStationInfos(){
		log.info("开始查询批量参数重置信息.......");
		Map<String, List> result  = new HashMap<String,List>();
		List<BatchStationBean> batchStationlist=null;
		String startTimeStr = this.getPara("startTime");
		String endTimeStr = this.getPara("endTime");
		Integer resetFlag = this.getParaToInt("resetFlag");
		log.info("基站割接查询参数----startTimeStr="+startTimeStr+";endTimeStr="+endTimeStr+";resetFlag="+resetFlag);
		BatchStationService batchStationService = null;
		try {
			 batchStationService = SpringContext.getBean("batchStationService", BatchStationService.class);
			batchStationlist = batchStationService.getBatchStationInfos(startTimeStr, endTimeStr, resetFlag);
		
		} catch (Exception e) {
			log.error("查询批量参数重置页面失败"+e.getMessage());
			e.printStackTrace();
		}
		result.put("batchStationlist", batchStationlist);
		this.setAttr("batchStationlist", batchStationlist);
		this.setAttr("startTime", startTimeStr);
		this.setAttr("endTime", endTimeStr);
		this.setAttr("resetFlag", resetFlag);
		this.render("/cpeManager/batchStationCutover.jsp");
		return;
	}
	/**
	 * 基站割接Excle文件上传
	 * @throws IOException 
	 */
	public void uploadBatchStationExcle() throws IOException{
		

		Map<String, Object> result  = new HashMap<String,Object>();
		this.getRequest().setCharacterEncoding("UTF-8");
		this.getResponse().setContentType("text/html; charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		log.info("进入上传uploadBatchStationExcle方法---------");
		// 上传文件的保存路径
		String configPath = "upload/";
		String dirTemp = "upload/temp/";
		String filePath = PropUtil.getProp("UPLOAD_EXCLE_PATH", "/config/aibi_pop/ftp.properties");
		// 文件保存目录路径
		String savePath = configPath;
		// 临时文件目录
		String tempPath = dirTemp;
		// 创建文件夹
		File dirFile = new File(filePath+savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
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
		BatchStationService batchStationService = null;
		String fileName="";
		try {
			List<?> items = upload.parseRequest(this.getRequest());
			Iterator<?> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				 fileName = item.getName();
				log.info("-------------开始上传文件"+fileName);
				long fileSize = item.getSize();
				if (!item.isFormField()) {
					OutputStream os = null;
					InputStream is =null;
					try {
						os = new FileOutputStream(filePath+savePath+fileName);
						is = item.getInputStream();
						byte buf[] = new byte[1024];// 可以修改 1024 以提高读取速度
						int length = 0;
						while ((length = is.read(buf)) > 0) {
							os.write(buf, 0, length);
						}
				
					}catch (Exception e) {
						e.printStackTrace();
					}finally{
					// 关闭流
						os.flush();
						os.close();
						is.close();
					}
				} 
				if(fileName!=null)
					break;
			
			}
			String id=this.saveUploadFile();
			log.info("-------------结束上传文件"+fileName);
			//文件上传成功后读取文件存入mysq库中
			List<ImportBatchCutoverBean> stationExcel = BatchStationUtil.readCpetStationExcel(filePath+savePath + fileName);
			for (ImportBatchCutoverBean importBatchCutoverBean : stationExcel) {
				
				System.out.println("读取上传文件转成bean对象："+importBatchCutoverBean.toString());
			}
			//调用service
				if(stationExcel.size()>0){
					batchStationService = SpringContext.getBean("batchStationService", BatchStationService.class);
					String res = batchStationService.cpeBatchStationImport(stationExcel);
					log.info("导入割接数据结束返回结果="+res);
					if("0".equals(res) || "1".equals(res)){
						this.setAttr("stationExcel", stationExcel);
						this.setAttr("flag", "1");
						result.put("stationExcel", stationExcel);
						result.put("flag", "1");
						result.put("message", "基站割接上传文件导入成功！");
					}else if("-1".equals("res")){
						this.setAttr("flag", "-1");
						result.put("flag", "-1");
						result.put("message", "基站割接上传文件导入失败");
					}
					
					
				}else{
					result.put("flag", "0");
					result.put("message", "基站割接上传失败,文件内容为空！");
				}
			 
		} catch (FileUploadException e) {
			result.put("flag", "-1");
			result.put("message", "基站割接上传文件异常");
			log.error("文件上传异常.......");
			e.printStackTrace();
		}catch (DataAccessException e) {
			result.put("flag", "-1");
			result.put("message", "基站割接上传文件导入失败");
			log.error("基站割接导入数据失败了:Mysql数据库访问异常");
		//	e.printStackTrace();
		}catch (Exception e) {
			result.put("flag", "-1");
			result.put("message", "基站割接上传文件导入失败");
			log.error("基站割接导入数据失败了:"+e.getMessage());
		//	e.printStackTrace();
		}
		
	
		log.info("------------割接文件上传结束!");
		File uploadFile = new File(filePath+savePath+fileName);
		if(uploadFile.exists()){
//			uploadFile.delete();
		}
		this.renderJson(result);
//		out.flush();
//		out.close();
		
	}
/**
 * 该方法不可少，上传文件需要调用该方法
 * @return
 */
	private String saveUploadFile() {
		final String id =PopUtil.generateId();
		boolean succeed = Db.tx(new IAtom() {

			@Override
			public boolean run() throws SQLException {

			
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
	public void batch() {
		log.info("进入基站割接页面.......");
		render("/cpeManager/batchStationCutover.jsp");
	}

	/**
	 * 基站割接模板下载
	 */
public void downLoadExcel(){
	String filePath = getFileTempletePath();
	String fileName="基站割接模板.xlsx";
	log.info("开始下载基站割接模板........");
	try {
		XSSFWorkbook book = ExcelUtil2007.getWorkBook(filePath+"/"+fileName);
		HttpServletResponse res= this.getResponse();
		 OutputStream output=res.getOutputStream();
		res.setHeader("Content-disposition", "attachment; filename="+java.net.URLEncoder.encode(fileName, "UTF-8"));
		res.setContentType("application/msexcel"); 
		book.write(output);
		if(output!=null)
			output.close();
		log.info("下载基站割接模板结束........");
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}finally{
		this.renderNull();
	}
	return;
}
public String getFileTempletePath(){
	return "/home/pop/upload/temp";
}


}
