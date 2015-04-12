package co.je.conductor.domain.business;

import co.je.conductor.domain.entities.ConcurrencySpecs;

public class ConcurrencyValidator {

    public static ConcurrencySpecs getCorrectedConcurrencySpecs(ConcurrencySpecs originalConcurrencySpecs) {

        ConcurrencySpecs correctedConcurrencySpecs = originalConcurrencySpecs;

        int totalCalls = originalConcurrencySpecs.getTotalCalls();
        int concurrentCalls = originalConcurrencySpecs.getConcurrentCalls();

        if (concurrentCalls > totalCalls) {

            int correctedConcurrentCalls = totalCalls;
            correctedConcurrencySpecs = new ConcurrencySpecs(totalCalls, correctedConcurrentCalls);
        }

        return correctedConcurrencySpecs;
    }
}