package co.je.conductor.domain.business;

import static org.junit.Assert.*;

import co.je.conductor.domain.entities.ExecutionSpecs;
import org.junit.Test;

public class ConcurrencyValidatorTest {
    
    @Test
    public void testGetCorrectedConcurrencySpecs_OK() {
        
        int totalCalls = 100;
        int concurrentCalls = 10;
        ExecutionSpecs executionSpecs = new ExecutionSpecs(totalCalls, concurrentCalls);
        ExecutionSpecs correctedExecutionSpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(executionSpecs);
        
        assertNotNull(correctedExecutionSpecs);
        assertEquals(0, executionSpecs.compareTo(correctedExecutionSpecs));
    }
    
    @Test
    public void testGetCorrectedConcurrencySpecs_NOK_concurrentCallsEqualsTotalCalls() {
        
        int totalCalls = 10;
        int concurrentCalls = 10;
        ExecutionSpecs executionSpecs = new ExecutionSpecs(totalCalls, concurrentCalls);
        ExecutionSpecs correctedExecutionSpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(executionSpecs);
        
        assertNotNull(correctedExecutionSpecs);
        assertEquals(0, executionSpecs.compareTo(correctedExecutionSpecs));
    }
    
    @Test
    public void testGetCorrectedConcurrencySpecs_NOK_concurrentCallsGreaterThanTotalCalls() {
        
        int totalCalls = 10;
        int concurrentCalls = 100;
        ExecutionSpecs executionSpecs = new ExecutionSpecs(totalCalls, concurrentCalls);
        ExecutionSpecs correctedExecutionSpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(executionSpecs);
        
        assertNotNull(correctedExecutionSpecs);
        assertEquals(totalCalls, correctedExecutionSpecs.getTotalCalls());
        assertEquals(totalCalls, correctedExecutionSpecs.getParallelCalls());
    }
}