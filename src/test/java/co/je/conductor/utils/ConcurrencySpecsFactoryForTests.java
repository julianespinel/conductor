package co.je.conductor.utils;

import co.je.conductor.domain.entities.ExecutionSpecs;

public class ConcurrencySpecsFactoryForTests {
    
    public static ExecutionSpecs getConcurrencySpecs() {

        int totalCalls = 10;
        int concurrentCalls = 5;
        ExecutionSpecs executionSpecs = new ExecutionSpecs(totalCalls, concurrentCalls);
        return executionSpecs;
    }
}