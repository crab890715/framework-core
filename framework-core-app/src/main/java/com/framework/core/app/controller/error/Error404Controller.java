package com.framework.core.app.controller.error;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class Error404Controller implements ErrorController{
	private static final String ERROR_PATH = "/error"; 
	@RequestMapping(value=ERROR_PATH)  
    public ModelAndView handleError(){  
        return new ModelAndView("error/404");  
    }
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
