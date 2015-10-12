package co.je.conductor.domain.business;

import co.je.conductor.domain.entities.ExecutionSpecs;

public class ConcurrencyValidator {

    public static ExecutionSpecs getCorrectedConcurrencySpecs(ExecutionSpecs originalExecutionSpecs) {

        ExecutionSpecs correctedExecutionSpecs = originalExecutionSpecs;

        int totalCalls = originalExecutionSpecs.getTotalCalls();
        int concurrentCalls = originalExecutionSpecs.getParallelCalls();

        if (concurrentCalls > totalCalls) {

            int correctedConcurrentCalls = totalCalls;
            correctedExecutionSpecs = new ExecutionSpecs(totalCalls, correctedConcurrentCalls);
        }

        return correctedExecutionSpecs;
    }
}