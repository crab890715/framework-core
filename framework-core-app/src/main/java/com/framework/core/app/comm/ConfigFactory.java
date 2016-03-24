package com.framework.core.app.comm;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
/**
 * 配置工厂类，将一些需要在程序代码中使用的配置信息通过配置文件加载到Java缓存中以便于使用
 * @author crab_liu
 *
 */
public class ConfigFactory implements InitializingBean{
	private static Properties properties = new Properties();
	private static String[] locations;
	@Override
	public void afterPropertiesSet() throws Exception {
		//FileInputStream stream = null;
		if(locations!=null){
			for(String path : locations){
    			//stream = new FileInputStream(path);
    			properties.load(this.getClass().getResourceAsStream(path));
    			//stream.close();
			}
		}
	}
	/**
	 * @return the properties
	 */
	public static Properties getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public static void setProperties(Properties properties) {
		ConfigFactory.properties = properties;
	}
	public static String getString(String key){
        return properties.getProperty(key);
    }
	/**
	 * @return the locations
	 */
	public static String[] getLocations() {
		return locations;
	}
	/**
	 * @param locations the locations to set
	 */
	public static void setLocations(String[] locations) {
		ConfigFactory.locations = locations;
	}
}
