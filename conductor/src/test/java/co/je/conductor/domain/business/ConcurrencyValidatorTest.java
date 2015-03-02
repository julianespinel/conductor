package co.je.conductor.domain.business;

import static org.junit.Assert.*;

import org.junit.Test;

import co.je.conductor.domain.entities.ConcurrencySpecs;

public class ConcurrencyValidatorTest {
    
    @Test
    public void testGetCorrectedConcurrencySpecs_OK() {
        
        int totalCalls = 100;
        int concurrentCalls = 10;
        ConcurrencySpecs concurrencySpecs = new ConcurrencySpecs(totalCalls, concurrentCalls);
        ConcurrencySpecs correctedConcurrencySpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(concurrencySpecs);
        
        assertNotNull(correctedConcurrencySpecs);
        assertEquals(0, concurrencySpecs.compareTo(correctedConcurrencySpecs));
    }
    
    @Test
    public void testGetCorrectedConcurrencySpecs_NOK_concurrentCallsEqualsTotalCalls() {
        
        int totalCalls = 10;
        int concurrentCalls = 10;
        ConcurrencySpecs concurrencySpecs = new ConcurrencySpecs(totalCalls, concurrentCalls);
        ConcurrencySpecs correctedConcurrencySpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(concurrencySpecs);
        
        assertNotNull(correctedConcurrencySpecs);
        assertEquals(0, concurrencySpecs.compareTo(correctedConcurrencySpecs));
    }
    
    @Test
    public void testGetCorrectedConcurrencySpecs_NOK_concurrentCallsGreaterThanTotalCalls() {
        
        int totalCalls = 10;
        int concurrentCalls = 100;
        ConcurrencySpecs concurrencySpecs = new ConcurrencySpecs(totalCalls, concurrentCalls);
        ConcurrencySpecs correctedConcurrencySpecs = ConcurrencyValidator.getCorrectedConcurrencySpecs(concurrencySpecs);
        
        assertNotNull(correctedConcurrencySpecs);
        assertEquals(totalCalls, correctedConcurrencySpecs.getTotalCalls());
        assertEquals(totalCalls, correctedConcurrencySpecs.getConcurrentCalls());
    }
}