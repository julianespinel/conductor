package co.je.conductor.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessException implements IException {
	
	private final String type;
	private final String message;
	
	@JsonCreator
	public BusinessException(@JsonProperty("message") String message) {
		
		this.type = ExceptionTypes.BUSINESS.getName();
		this.message = message;
	}

	@Override
	public String getType() {
		
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

    @Override
    public int compareTo(IException exception) {
        
        int answer = 0;
        
        answer += type.compareToIgnoreCase(exception.getType());
        answer += message.compareToIgnoreCase(exception.getMessage());
        
        return answer;
    }
}