// File: scheduler/models/Process

package scheduler.models;

/**
 * Represents a single process in the scheduling system.
 * Contains attributes and methods to track process properties and scheduling metrics.
 */
public class Process {
    private String name; // Identifier for the process
    private int arrivalTime; // Time at which the process arrives
    private int burstTime; // Total execution time required by the process
    private int remainingTime; // Remaining execution time (for algorithms like Round Robin)
    private int startTime; // Time when the process starts execution
    private int completionTime; // Time when the process finishes execution
    private int turnaroundTime; // Total time from arrival to completion (completionTime - arrivalTime)
    private int waitingTime; // Total time spent waiting (turnaroundTime - burstTime)
    private int priority; // Priority of the process (used in priority scheduling)

    /**
     * Constructor for initializing a process with its core attributes.
     *
     * @param name        Name or identifier of the process.
     * @param arrivalTime Time at which the process arrives in the system.
     * @param burstTime   Total execution time required by the process.
     * @param priority    Priority of the process (lower value indicates higher priority).
     */
    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.startTime = 0; // Default to 0
        this.remainingTime = burstTime; // Initialize remaining time to burst time
    }

    // ----------- Getters and Setters -----------

    /**
     * @return Name or identifier of the process.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Arrival time of the process.
     */
    public int getArrivalTime() {
        return arrivalTime;
    }

    /**
     * @return Total execution time required by the process.
     */
    public int getBurstTime() {
        return burstTime;
    }

    /**
     * @return Remaining execution time for the process (for Round Robin or similar algorithms).
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Sets the remaining execution time.
     *
     * @param remainingTime Updated remaining time.
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**
     * @return Start time of the process.
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time for the process.
     *
     * @param startTime Time when the process starts execution.
     */
    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Completion time of the process.
     */
    public int getCompletionTime() {
        return completionTime;
    }

    /**
     * Sets the completion time for the process.
     *
     * @param completionTime Time when the process finishes execution.
     */
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * @return Turnaround time of the process (completionTime - arrivalTime).
     */
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    /**
     * Sets the turnaround time for the process.
     *
     * @param turnaroundTime Calculated turnaround time.
     */
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    /**
     * @return Waiting time of the process (turnaroundTime - burstTime).
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Sets the waiting time for the process.
     *
     * @param waitingTime Calculated waiting time.
     */
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    /**
     * @return Priority of the process (used in priority scheduling).
     */
    public int getPriority() {
        return priority;
    }
    // toString() method for debugging and logging
    @Override
    public String toString() {
        return String.format(
                "Process{name='%s', arrivalTime=%d, burstTime=%d, remainingTime=%d, startTime=%d, completionTime=%d, turnaroundTime=%d, waitingTime=%d, priority=%d}",
                name, arrivalTime, burstTime, remainingTime, startTime, completionTime, turnaroundTime, waitingTime, priority
        );
    }
}
