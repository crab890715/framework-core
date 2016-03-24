package com.beacon.wechat.app.controller.wechat;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beacon.wechat.app.comm.Result;
import com.beacon.wechat.app.vo.SignInfoVO;

@RestController 
@RequestMapping("/sign")
public class SignController {
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public Result<Map<String,Object>> create(SignInfoVO signinfo){
		return new Result<>();
	}
}
