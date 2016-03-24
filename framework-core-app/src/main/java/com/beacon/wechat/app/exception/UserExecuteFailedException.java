package com.beacon.wechat.app.exception;

public class UserExecuteFailedException extends RuntimeException {

	private static final long serialVersionUID = -8356076540236024894L;

	private Integer code;
	
	public UserExecuteFailedException(){
        super();
    }
    
    
    public UserExecuteFailedException(String message){
        super(message);
    }
    
    
    public UserExecuteFailedException(String message,Throwable cause){
        super(message,cause);
    }
    
    public UserExecuteFailedException(Throwable cause){
        super(cause);
    }
    
    public UserExecuteFailedException(Integer code,String message) {
    	super(message);
    	this.setCode(code);
    }


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}
}
