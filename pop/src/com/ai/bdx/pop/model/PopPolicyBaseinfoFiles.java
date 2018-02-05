package com.ai.bdx.pop.model;

import com.ai.bdx.pop.base.PopModel;
/** pop_policy_baseinfo_files
  id varchar(50) NOT NULL, --主键
  policy_id varchar(50) DEFAULT NULL, --策略ID
  rule_id varchar(50) DEFAULT NULL,--规则ID
  filename varchar(300) DEFAULT NULL,--文件名称
  filepath varchar(1000) DEFAULT NULL,--文件路径
  manufacturers varchar(20) DEFAULT NULL,--厂商
  createuserid varchar(255) DEFAULT NULL,--上传用户
  create_date datetime --创建时间
*/
public class PopPolicyBaseinfoFiles extends PopModel<PopPolicyBaseinfoFiles> {
	private static final long serialVersionUID = 1429169706225L;

	public static PopPolicyBaseinfoFiles dao(){
		return new PopPolicyBaseinfoFiles();
	}
	public static final String COL_ID = "id";
	public static final String COL_POLICY_ID = "policy_id";
	public static final String COL_RULE_ID = "rule_id";
	public static final String COL_FILENAME = "filename";
	public static final String COL_FILEPATH = "filepath";
	public static final String COL_MANUFACTURERS = "manufacturers";
	public static final String COL_CREATEUSERID = "createuserid";
	public static final String COL_CREATE_DATE = "create_date";
	

}
