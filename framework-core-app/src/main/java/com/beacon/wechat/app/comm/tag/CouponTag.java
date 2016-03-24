package com.beacon.wechat.app.comm.tag;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;

import com.beacon.wechat.app.comm.utils.AppUtils;

public class CouponTag extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8997239144745198831L;
	private String id;
	private String src;
	private String name;
	private String style;
	private String css;
	private String height;
	private String width;
	private String alt;
	private String type="tag";
	public String version(String uri){
		uri=StringUtils.isBlank(uri)?"":uri;
		if(uri.indexOf("?")>-1){
			uri+="&v="+AppUtils.version();
		}else{
			uri+="?v="+AppUtils.version();
		}
		return uri;
	}
	/**
	 * @return the src
	 */
	public String getSrc() {
		return src;
	}
	private void addAttribute(StringBuffer sb,String name,String value){
		if(StringUtils.isNotBlank(value)){
			sb.append(MessageFormat.format(" {0}=\"{1}\" ",name,value));
		}
	}
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		String url = AppUtils.coupon(version(src));
		StringBuffer sb = new StringBuffer();
		addAttribute(sb, "id", id);
		addAttribute(sb, "src", url);
		addAttribute(sb, "name", name);
		addAttribute(sb, "class", css);
		addAttribute(sb, "height", height);
		addAttribute(sb, "width", width);
		addAttribute(sb, "alt", alt);
		addAttribute(sb, "style", style);
        try {
        	if("image".equals(type)){
        		out.println(url);
        	}else{
        		out.println( MessageFormat.format("<img{0}></img>",
        				sb.toString()));
        	}
			
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

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the css
	 */
	public String getCss() {
		return css;
	}

	/**
	 * @param css the css to set
	 */
	public void setCss(String css) {
		this.css = css;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the alt
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * @param alt the alt to set
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
