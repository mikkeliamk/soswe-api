package com.belvain.soswe.soswe_api;

public class TypeNotSupportedException extends Exception {

	private static final long serialVersionUID = -156654708301226905L;
	
	public TypeNotSupportedException(String msg){
		super(msg);
	}
	public String getMessage(){
		return super.getMessage();
	}
}
