package co.je.conductor.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TechnicalException implements IException {
	
	private final String type;
	private final String message;
	
	@JsonCreator
	public TechnicalException(@JsonProperty("message") String message) {
		
		this.type = ExceptionTypes.TECHNICAL.getName();
		this.message = message;
	}

	@Override
	public String getExceptionType() {
		
		return type;
	}

	@Override
	public String getMessage() {
		
		return message;
	}
	
	@Override
    public boolean isBusinessException() {
        
        return (type.equalsIgnoreCase(ExceptionTypes.BUSINESS.getName()));
    }
}