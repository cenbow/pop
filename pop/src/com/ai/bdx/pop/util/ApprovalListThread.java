package com.ai.bdx.pop.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ai.bdx.frame.approval.service.IApprovalService;
import com.asiainfo.biframe.utils.spring.SystemServiceLocator;
public class ApprovalListThread extends Thread{
	
	private static Logger log = LogManager.getLogger();
	
	private IApprovalService approvalService=null;
	
	private String approvalFlowId;//流程ID
	private String approvalId;//待审批的策略ID
	private Integer status;//待审批策略的状态
	private String needApprovalUserid;//用户id
	private String drvTypeID;//驱动分类ID
	private String drvID;//驱动ID

	
	public  ApprovalListThread(String approvalId,Integer status,String needApprovalUserid,String drvTypeID,String drvID){
		this.approvalId=approvalId;
		this.status=status;
		this.needApprovalUserid=needApprovalUserid;
		this.drvTypeID=drvTypeID;
		this.drvID=drvID;
		String threadName=approvalId+"审批确认过程开始初始化";
		log.debug("{},参数信息是：approvalId={},status={},needApprovalUserid={},drvTypeID={},drvID={},approvalType={}",threadName,approvalId,status,needApprovalUserid,drvTypeID,drvID);
		this.setName(threadName);
	}
	public void run() {
		try {
			approvalService = (IApprovalService) SystemServiceLocator.getInstance().getService(ApprovalCONST.APPROVE_SERVICE);
			approvalService.doApproval(approvalId, status, needApprovalUserid, drvTypeID, drvID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getApprovalFlowId() {
		return approvalFlowId;
	}
	public void setApprovalFlowId(String approvalFlowId) {
		this.approvalFlowId = approvalFlowId;
	}
	public String getNeedApprovalUserid() {
		return needApprovalUserid;
	}
	public void setNeedApprovalUserid(String needApprovalUserid) {
		this.needApprovalUserid = needApprovalUserid;
	}
	public String getDrvTypeID() {
		return drvTypeID;
	}
	public void setDrvTypeID(String drvTypeID) {
		this.drvTypeID = drvTypeID;
	}
	public String getDrvID() {
		return drvID;
	}
	public void setDrvID(String drvID) {
		this.drvID = drvID;
	}
	public String getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public IApprovalService getApprovalService() {
		return approvalService;
	}

	public void setApprovalService(IApprovalService approvalService) {
		this.approvalService = approvalService;
	}
	
}
