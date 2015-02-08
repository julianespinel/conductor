package co.je.conductor.infrastructure.exceptions;

public enum ExceptionTypes {
	
	TECHNICAL("technical"),
	BUSINESS("business");
	
	private final String name;

	private ExceptionTypes(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}