package co.je.conductor.domain.entities;

public class ConcurrencySpecs {
	
	private final int totalCalls;
	private final int concurrentCalls;
	
	public ConcurrencySpecs(int totalCalls, int concurrentCalls) {
		
		this.totalCalls = totalCalls;
		this.concurrentCalls = concurrentCalls;
	}

	public int getTotalCalls() {
		return totalCalls;
	}

	public int getConcurrentCalls() {
		return concurrentCalls;
	}
}