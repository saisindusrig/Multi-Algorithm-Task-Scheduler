// File: scheduler/algorithms/Scheduler.java

package scheduler.algorithms;

import scheduler.models.Process;
import java.util.List;

/**
 * Abstract base class for scheduling algorithms.
 * This class provides the foundation for various scheduling strategies
 * by defining shared functionality and enforcing the `schedule` method.
 */
public abstract class Scheduler {

    // List of processes to be scheduled
    protected List<Process> processes;

    /**
     * Constructor to initialize the Scheduler with a list of processes.
     *
     * @param processes A list of processes that need to be scheduled.
     */
    public Scheduler(List<Process> processes) {
        this.processes = processes;
    }

    /**
     * Abstract method to implement the scheduling logic.
     * Subclasses must override this method to provide specific scheduling behavior.
     */
    public abstract void schedule();

    /**
     * Get the list of processes being managed by the scheduler.
     *
     * @return The list of processes.
     */
    public List<Process> getProcesses() {
        return processes;
    }
}
