// File: scheduler/algorithms/FCFS.java
package scheduler.algorithms;

import scheduler.models.Process;
import java.util.List;

/**
 * FCFS (First-Come, First-Served) scheduling algorithm.
 * This scheduler processes tasks in the order of their arrival time.
 */
public class FCFS extends Scheduler {

    /**
     * Constructor for FCFS Scheduler.
     *
     * @param processes List of processes to schedule.
     */
    public FCFS(List<Process> processes) {
        super(processes);
    }

    /**
     * Implements the FCFS scheduling logic.
     * Processes are executed in the order they arrive.
     * This method calculates the start time, completion time,
     * turnaround time, and waiting time for each process.
     */
    @Override
    public void schedule() {
        int currentTime = 0; // Keeps track of the current time in the system.

        for (Process process : processes) {
            // Handle idle time (CPU is idle if no process has arrived yet).
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            // Set the process's start time to the current time.
            process.setStartTime(currentTime);

            // Calculate and set the completion time for the process.
            process.setCompletionTime(currentTime + process.getBurstTime());

            // Calculate and set the turnaround time: total time in the system.
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());

            // Calculate and set the waiting time: time spent waiting to be executed.
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            // Update the current time to reflect the end of the process execution.
            currentTime = process.getCompletionTime();
        }
    }
}
