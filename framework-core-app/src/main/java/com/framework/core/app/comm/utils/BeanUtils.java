package com.framework.core.app.comm.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

public class BeanUtils {
	private static Logger log = Logger.getLogger(BeanUtils.class);
	private static final String[] ftilerFieldNames = {"serialVersionUID","ID","id"};
	/**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer=new DozerBeanMapper();

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source,Class<T> destinationClass){
    	if(source==null)source=new Object();
        return dozer.map(source,destinationClass);
    }

    
    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <T> List<T> mapList(Collection<Object> sourceList,Class<T> destinationClass){
        List<T> destinationList=Lists.newArrayList();
        for(Object sourceObject : sourceList){
            T destinationObject=dozer.map(sourceObject,destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source,Object destinationObject){
        dozer.map(source,destinationObject);
    }
    
    /**
	* @Title: convertBean 
	* @Description: 返回一个targetClass的实例，这个返回结果复制了oriBean里同名同类型的字段的值
	* @param @param oriBean
	* @param @param targetClass
	* @param @return    设定文件 
	* @return T    返回类型 
	* @throws
	 */
	public static <T> T convertBean(Object oriBean, Class<T> targetClass){
		
		if(oriBean == null){
			return null;
		}
		
		Field[] oriFields = oriBean.getClass().getDeclaredFields();
		Field[] targetFields = targetClass.getDeclaredFields();
		
		Map<String,Field> oriNameFieldMap = makeNameMapping(oriFields);
		
		T targetBean = null;
		try {
			targetBean = targetClass.newInstance();
		} catch (InstantiationException e) {
			log.error(e);
		} catch (IllegalAccessException e) {
			log.error(e);
		}
		
		for(Field targetField : targetFields){
			
			if(filterNames(targetField.getName())){
				continue;
			}
			
			targetField.setAccessible(true);
			
			if(oriNameFieldMap.containsKey(targetField.getName())){
				Field oriField = oriNameFieldMap.get(targetField.getName());
				if(oriField.getType().getName().equals(targetField.getType().getName())){
					oriField.setAccessible(true);
					
					try {
						targetField.set(targetBean, oriField.get(oriBean));
					} catch (IllegalArgumentException e) {
						log.error(e);
					} catch (IllegalAccessException e) {
						log.error(e);
					}
				}
			}
		}
		return targetBean;
	}
	
	/**
	* @Title: filterNames 
	* @Description: 判断是否过滤字段
	* @param @param fieldName
	* @param @return    设定文件 
	* @return boolean    返回类型 
	* @throws
	 */
	private static boolean filterNames(String fieldName){
		for(String ftilerFieldName : ftilerFieldNames){
			if(fieldName.equals(ftilerFieldName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	* @Title: makeNameMapping 
	* @Description: 创建名字-field的map
	* @param @param fields
	* @param @return    设定文件 
	* @return Map<String,Field>    返回类型 
	* @throws
	 */
	private static Map<String,Field> makeNameMapping(Field[] fields){
		Map<String,Field> map = new HashMap<String,Field>();
		for(Field field : fields){
			map.put(field.getName(), field);
		}
		return map;
	}
}
