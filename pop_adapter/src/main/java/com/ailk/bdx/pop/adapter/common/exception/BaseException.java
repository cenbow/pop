/*
 * @(#)DamException.java
 *
 * CopyRight (c) 2013 亚信联创科技(中国)有限公司 保留所有权利。
 */

package  com.ailk.bdx.pop.adapter.common.exception;

import com.asiainfo.biframe.exception.BaseRuntimeException;



/**
 * Title : DamException
 * <p/>
 * Description : 数据资产管控产品错误信息封装类
 * <p/>
 * CopyRight : CopyRight (c) 2013
 * <p/>
 * Company : 亚信联创科技(中国)有限公司
 * <p/>
 * JDK Version Used : JDK 5.0 +
 * <p/>
 * Modification History	:
 * <p/>
 * <pre>NO.    Date    Modified By    Why & What is modified</pre>
 * <pre>1    2013-6-14    wuwl        Created</pre>
 * <p/>
 *
 * @author  wuwl
 * @version 1.0.0.2013-6-14
 */
public class BaseException extends BaseRuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
