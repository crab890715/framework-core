package com.beacon.wechat.api.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao <T,ID extends Serializable>{
	    public T insert(T t);
	    public int insertBatch(List<T> t);
	    public int deleteBatchById(List<ID> ids);
	    public int deleteById(ID id);
	    public int update(T t);
	    public T find(Map<String, Object> parameter);
	    public T findById(ID id);
	    public List<T> queryListAll(Map<String, Object> parameter);
	    public List<T> queryListByPage(Map<String, Object> parameter);
	    public int count(Map<String, Object> parameter);
}
