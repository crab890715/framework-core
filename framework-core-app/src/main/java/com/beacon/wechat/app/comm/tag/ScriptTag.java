package com.beacon.wechat.app.comm.tag;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import com.beacon.wechat.app.comm.utils.AppUtils;

public class ScriptTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8997239144745198831L;
	private String id;
	private String src;
	
	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}
	public String version(String uri){
		uri=StringUtils.isBlank(uri)?"":uri;
		if(uri.indexOf("?")>-1){
			uri+="&v="+AppUtils.version();
		}else{
			uri+="?v="+AppUtils.version();
		}
		return uri;
	}
	private void addAttribute(StringBuffer sb,String name,String value){
		if(StringUtils.isNotBlank(value)){
			sb.append(MessageFormat.format(" {0}=\"{1}\" ",name,value));
		}
	}
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		String baseurl = AppUtils.baseurl();
		StringBuffer sb = new StringBuffer();
		addAttribute(sb, "id", id);
		addAttribute(sb, "src", baseurl+"/scripts/"+version(src));
        try {
			out.println( MessageFormat.format("<script type=\"text/javascript\"{0}></script>",
					sb.toString()) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doEndTag();
	}
	/**
	 * @param src the src to set
	 */
	public void setSrc(String src) {
		this.src = src;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
