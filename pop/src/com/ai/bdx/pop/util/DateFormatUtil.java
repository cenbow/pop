
package com.ai.bdx.pop.util;

import com.asiainfo.biframe.utils.config.Configure;
import com.asiainfo.biframe.utils.database.jdbc.Sqlca;

/**
 * Title : DateFormatUtil
 * <p/>
 * Description : 时间格式处理类，兼容各种数据库
 * <p/>
 * CopyRight : CopyRight (c) 2013
 * <p/>
 * Company : 亚信联创科技(中国)有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History :
 * <p/>
 * 
 * <pre>
 * NO.    Date    Modified By    Why & What is modified
 * </pre>
 * 
 * <pre>
 * 1    2013-6-17    chenjq        Created
 * </pre>
 * <p/>
 * 
 * @author chenjq
 * @version 1.0.0.2013-6-17
 */

public class DateFormatUtil{
    
    /**
     * Description: 将string转换为timestamp类型
     *
     * @param ts
     * @return
     * @throws Exception 
     */
    public static String string2timestamp(String ts) throws Exception{
        String db_type;
        try {
           db_type = Configure.getInstance().getProperty("DBTYPE");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        String restr="";
        if (db_type.equalsIgnoreCase(PopConstant.DB_TYPE_DB2)) {
            restr = "TIMESTAMP('"+ts+"')";
        } else if (db_type.equalsIgnoreCase(PopConstant.DB_TYPE_ORACLE)) {
            restr = "TO_TIMESTAMP('"+ts+"','YYYY-MM-DD HH24:MI:SS')";
        }
        return restr;
    }
    
}
