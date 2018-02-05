package com.ai.bdx.pop.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ai.bdx.pop.util.PopUtil;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.jfinal.plugin.activerecord.cache.ICache;

/**
 * The Class CepModel.
 *
 * @param <M> the generic type
 * @author wanglei
 */
@SuppressWarnings({ "rawtypes", "serial" })
public abstract class PopModel<M extends PopModel> extends Model<M> {
	
	/**
	 * Search first.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the m
	 */
	public M searchFirst(String key, Object value) {
		List<M> mList = search(key, value, "");
		return mList != null && mList.size() > 0 ? mList.get(0) : null;
	}

	/**
	 * Search.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the list
	 */
	public List<M> search(String key, Object value) {
		return search(key, value, "");
	}

	/**
	 * Search.
	 *
	 * @param key the key
	 * @param value the value
	 * @param orderBy the order by
	 * @return the list
	 */
	public List<M> search(String key, Object value, String orderBy) {
		String sql = "select * from " + TableMapping.me().getTable(getClass()).getName() + " where " + key + "=? "
				+ orderBy;
		return find(sql, value);
	}

	/**
	 * Search first.
	 *
	 * @param maps the maps
	 * @return the m
	 */
	public M searchFirst(Map<String, Object> maps) {
		List<M> mList = search(maps, "");
		return mList != null && mList.size() > 0 ? mList.get(0) : null;
	}

	/**
	 * Search.
	 *
	 * @param maps the maps
	 * @return the list
	 */
	public List<M> search(Map<String, Object> maps) {
		return search(maps, "");
	}

	/**
	 * Search.
	 *
	 * @param maps the maps
	 * @param orderBy the order by
	 * @return the list
	 */
	public List<M> search(Map<String, Object> maps, String orderBy) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ").append(TableMapping.me().getTable(getClass()).getName()).append(" where 1=1 ");
		List<Object> values = new ArrayList<Object>();
		for (Entry<String, Object> entry : maps.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(" and ").append(entry.getKey()).append("=?");
				values.add(entry.getValue());
			}
		}
		sb.append(" ").append(orderBy);
		return find(sb.toString(), values.toArray());
	}

	/**
	 * Search by cache.
	 *
	 * @param cacheName the cache name
	 * @param key the key
	 * @param maps the maps
	 * @return the list
	 */
	public List<M> searchByCache(String cacheName, Object key, Map<String, Object> maps) {
		return this.searchByCache(cacheName, key, maps, "");
	}

	/**
	 * Search by cache.
	 *
	 * @param cacheName the cache name
	 * @param key the key
	 * @param maps the maps
	 * @param orderBy the order by
	 * @return the list
	 */
	public List<M> searchByCache(String cacheName, Object key, Map<String, Object> maps, String orderBy) {
		ICache cache = DbKit.getConfig().getCache();
		List<M> result = cache.get(cacheName, key);
		if (result == null) {
			result = search(maps);
			cache.put(cacheName, key, result);
		}
		return result;
	}

	/**
	 * Search paginate.
	 *
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maps the maps
	 * @return the page
	 */
	public Page<M> searchPaginate(int pageNumber, int pageSize, Map<String, Object> maps) {
		return this.searchPaginate(pageNumber, pageSize, maps, "");
	}

	/**
	 * Search paginate.
	 *
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maps the maps
	 * @param orderBy the order by
	 * @return the page
	 */
	public Page<M> searchPaginate(int pageNumber, int pageSize, Map<String, Object> maps, String orderBy) {
		StringBuilder sb = new StringBuilder();
		sb.append("from ").append(TableMapping.me().getTable(getClass()).getName()).append(" ")
				.append(TableMapping.me().getTable(getClass()).getName()).append(" where 1=1");
		List<Object> values = new ArrayList<Object>();
		for (Entry<String, Object> entry : maps.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(" and ").append(entry.getKey()).append("=?");
				values.add(entry.getValue());
			}
		}
		sb.append(orderBy);
		return paginate(pageNumber, pageSize, "select *", sb.toString(), values.toArray());
	}

	/**
	 * Search paginate.
	 *
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maps the maps
	 * @param orderBy the order by
	 * @return the page
	 */
	public Page<M> searchPaginate(int pageNumber, int pageSize, String selectStr, String fromStr) {
		return paginate(pageNumber, pageSize, selectStr, fromStr);
	}

	/**
	 * Search paginate by cache.
	 *
	 * @param cacheName the cache name
	 * @param key the key
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maps the maps
	 * @return the page
	 */
	public Page<M> searchPaginateByCache(String cacheName, Object key, int pageNumber, int pageSize,
			Map<String, Object> maps) {
		return this.searchPaginateByCache(cacheName, key, pageNumber, pageSize, maps, "");
	}

	/**
	 * Search paginate by cache.
	 *
	 * @param cacheName the cache name
	 * @param key the key
	 * @param pageNumber the page number
	 * @param pageSize the page size
	 * @param maps the maps
	 * @param orderBy the order by
	 * @return the page
	 */
	public Page<M> searchPaginateByCache(String cacheName, Object key, int pageNumber, int pageSize,
			Map<String, Object> maps, String orderBy) {
		ICache cache = DbKit.getConfig().getCache();
		Page<M> result = cache.get(cacheName, key);
		if (result == null) {
			result = searchPaginate(pageNumber, pageSize, maps, orderBy);
			cache.put(cacheName, key, result);
		}
		return result;
	}

	/**
	 * 查询所有数据
	 * @return
	 */
	public List<M> findAll() {
		Table table = TableMapping.me().getTable(this.getClass());
		return this.find(new StringBuilder("select * from ").append(table.getName()).toString());
	}
	
	/**
	 * 查询所有数据（缓存模式）
	 * @return
	 */
	public List<M> findAllByCache(String cacheName, Object key) {
		ICache cache = DbKit.getConfig().getCache();
		List<M> result = cache.get(cacheName, key);
		if (result == null) {
			Table table = TableMapping.me().getTable(this.getClass());
			result = this.find(new StringBuilder("select * from ").append(table.getName()).toString());
			cache.put(cacheName, key, result);
		}
		return result;
	}

	/**
	 * 将值转换为bean对象
	 * @param clazz
	 * @return
	 */
	public <T> T toBean(Class<T> clazz) {
		try {
			T obj = clazz.newInstance();
			PopUtil.mapToBean(this.getAttrs(), obj);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 复制对象
	 * @param srcObject 源对象
	 * @return 复制后的新对象
	 */
	public PopModel<M> copyBean(PopModel<M> srcObject) {
		try {
			this.setAttrs(srcObject.getAttrs());
			return this;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
