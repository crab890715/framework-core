package com.framework.core.api.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class AbstractService<T, ID extends Serializable> implements BaseService<T, ID> {
	public abstract  BaseDao<T, ID> getBaseDao();
	@Override
	public T insert(T t) {
		return getBaseDao().insert(t);
	}

	@Override
	public int insertBatch(List<T> t) {
		return getBaseDao().insertBatch(t);
	}

	@Override
	public int deleteBatchById(List<ID> ids) {
		return getBaseDao().deleteBatchById(ids);
	}

	@Override
	public int deleteById(ID id) {
		return getBaseDao().deleteById(id);
	}

	@Override
	public int update(T t) {
		return getBaseDao().update(t);
	}

	@Override
	public T find(Map<String, Object> parameter) {
		return getBaseDao().find(parameter);
	}

	@Override
	public T findById(ID id) {
		return getBaseDao().findById(id);
	}

	@Override
	public List<T> queryListAll(Map<String, Object> parameter) {
		return getBaseDao().queryListAll(parameter);
	}

	@Override
	public List<T> queryListByPage(Map<String, Object> parameter) {
		return getBaseDao().queryListByPage(parameter);
	}

	@Override
	public int count(Map<String, Object> parameter) {
		return getBaseDao().count(parameter);
	}

}
