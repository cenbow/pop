package com.ai.bdx.pop.base;

import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;

/**
 * DB2的Dialect
 * 
 * @author wanglei
 *
 */
public class Db2Dialect extends AnsiSqlDialect {

	@Override
	public void forPaginate(StringBuilder sql, int pageNumber, int pageSize,
			String select, String sqlExceptSelect) {
		int satrt = (pageNumber - 1) * pageSize + 1;
		int end = pageNumber * pageSize;			
		
		if ( satrt == 1 ) {
			sql.append(select).append(" ");
			sql.append(sqlExceptSelect);
			sql.append(" fetch first " + end + " rows only");
		}
		sql.append("select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
				+ select +" " + sqlExceptSelect +" fetch first " + end + " rows only ) as inner2_ ) as inner1_ where rownumber_ > "
				+ satrt + " order by rownumber_");
		
	}

}
