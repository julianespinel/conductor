package co.je.conductor.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = BusinessException.class)
public interface IException extends Comparable<IException> {
    
	public String getType();
	
	public String getMessage();
	
	@JsonIgnore
	public boolean isBusinessException();
}