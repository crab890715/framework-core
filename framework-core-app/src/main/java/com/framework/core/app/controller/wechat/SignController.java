package com.framework.core.app.controller.wechat;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.framework.core.app.comm.Result;
import com.framework.core.app.vo.SignInfoVO;

@RestController 
@RequestMapping("/sign")
public class SignController {
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public Result<Map<String,Object>> create(SignInfoVO signinfo){
		return new Result<>();
	}
}
