package co.je.conductor.utils;

import co.je.conductor.domain.entities.ConcurrencySpecs;

public class ConcurrencySpecsFactoryForTests {
    
    public static ConcurrencySpecs getConcurrencySpecs() {

        int totalCalls = 10;
        int concurrentCalls = 5;
        ConcurrencySpecs concurrencySpecs = new ConcurrencySpecs(totalCalls, concurrentCalls);
        return concurrencySpecs;
    }
}