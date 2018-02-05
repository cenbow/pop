package com.ailk.bdx.cep.wservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * cep 外部服务接口
 * @author luozn
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface ICepWsService {

	/**
	 * 
	 * @param sysId  	系统ID
	 * @param templateId	EPL模版ID
	 * @param paramValues		EPL输入参数值
	 * @return
	 */
	@WebMethod
	@WebResult(name = "result")
	public String createEplInstance(@WebParam(name = "sysId") String sysId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "paramValues") String paramValues);

	/**
	 * 
	 * @param sysId  	系统ID
	 * @param templateId	EPL模版ID
	 * @param paramValues		EPL输入参数值
	 * @param startFlag 是否启动，0-不启动；1-启动
	 * @return
	 */
	@WebMethod
	@WebResult(name = "result")
	public String createEplInstanceWithStartFlag(@WebParam(name = "sysId") String sysId,
			@WebParam(name = "templateId") String templateId, @WebParam(name = "paramValues") String paramValues,
			@WebParam(name = "startFlag") String startFlag);

	/**
	 * 
	 * @param sysId  	系统ID
	 * @param eplId	EPL实例ID
	 * @param ctrlType 控制类型(2-暂停，3-终止，4-重启)
	 * @return
	 */
	@WebMethod
	@WebResult(name = "result")
	public String changeEplInstanceStatus(@WebParam(name = "sysId") String sysId,
			@WebParam(name = "eplId") String eplId, @WebParam(name = "ctrlType") String ctrlType);

	/**
	 * 
	 * @param sysId  	系统ID
	 * @param eplId	EPL实例ID
	 * @return
	 */
	@WebMethod
	@WebResult(name = "result")
	public String deleteEplInstance(@WebParam(name = "sysId") String sysId, @WebParam(name = "eplId") String eplId);
	/**
	 * 
	   * @Description:  查询EPLID下的lacci
	   * @param sysId	系统ID
	   * @param eplId	EPL实例ID
	   * @param pageSize   每页展示的个数
	   * @param page       第几页
	   * @Return: String
	 */
	@WebMethod
	@WebResult(name = "result")
	public String queryEplLacciInfo(@WebParam(name = "sysId") String sysId, @WebParam(name = "eplId") String eplId,@WebParam(name = "pageSize") int pageSize, @WebParam(name = "page") int page);
}
