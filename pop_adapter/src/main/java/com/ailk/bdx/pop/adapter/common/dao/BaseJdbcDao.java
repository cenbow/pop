/**
 * Filename: BaseJdbcDao.java Description: Copyright: Copyright (c)2011
 * 
 * @author: bob
 * @version: 1.0 Create at: 2013-6-4 上午9:34:05 Modification History: Date Author
 *           Version Description
 *           ------------------------------------------------------------------
 *           2013-6-4 bob 1.0 Create
 */

package com.ailk.bdx.pop.adapter.common.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ailk.bdx.pop.adapter.common.dao.constant.Constants;
import com.ailk.bdx.pop.adapter.common.model.POPAdapterDw;
import com.ailk.bdx.pop.adapter.util.DES;
import com.ailk.bdx.pop.adapter.util.SpringContext;

/**
 * @ClassName: BaseJdbcDao
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author bob
 * @date 2013-6-4 上午9:34:05
 */
public class BaseJdbcDao {

    private static final Log log = LogFactory.getLog(BaseJdbcDao.class);

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    /**
     * @param dataSource
     *        要设置的 dataSource
     */

    public void setDataSource(DataSource dataSource) {
        this.dataSource = SpringContext.getBean("dataSource",BasicDataSource.class);
        
    }

    public JdbcTemplate getJdbcTemplate() {
        if (dataSource == null)
            return jdbcTemplate;
        else
            return jdbcTemplate = SpringContext.getBean("jdbcTemplate",JdbcTemplate.class);
    }

    @Resource(name = "dpDataSource")
    public void setJdbcTemplate(DataSource damDataSource) {
        if (this.dataSource == null)
            this.jdbcTemplate = SpringContext.getBean("jdbcTemplate",JdbcTemplate.class);
        else
            this.jdbcTemplate = this.getJdbcTemplate();
    }

    // 创建数据库链接，可以根据数据库的配置信息获取数据库链接．
    public Connection getConnection(POPAdapterDw popDw) throws SQLException, Exception {
        BasicDataSource ds = new BasicDataSource();
        ds=(BasicDataSource) getDataSorce(popDw);
        return ds.getConnection();
    }
    //创建数据仓库数据源，根据数据库的配置获取数据库链接
    public DataSource getDataSorce(POPAdapterDw popDw){
        BasicDataSource ds = new BasicDataSource();
        if (Constants.DW_TYPE_DB2 == popDw.getDwTypeId()) {
            ds.setDriverClassName(Constants.DB2DRIVER);
        } else if (Constants.DW_TYPE_ORACLE == popDw.getDwTypeId()) {
            ds.setDriverClassName(Constants.ORACLEDRIVER);
        } else {
            log.error("不支持的数据库类型,只支持oracle或db2数据库．");
        }
        ds.setUrl(popDw.getDwAddr());
        ds.setUsername(popDw.getDwUsername());
        String password = null;
        try {
            password = DES.decrypt(popDw.getDwPassword());
        } catch (Exception e) {            
            password = popDw.getDwPassword();
        }
        
        ds.setPassword(password);
        return ds;
    }

}
