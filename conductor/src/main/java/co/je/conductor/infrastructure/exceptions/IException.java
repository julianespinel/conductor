package co.je.conductor.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IException {
	
	public String getExceptionType();
	
	public String getMessage();
	
	@JsonIgnore
	public boolean isBusinessException();
}