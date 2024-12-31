// File: scheduler/algorithms/RR.java

package scheduler.algorithms;

import scheduler.models.Process;

import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

/**
 * RR (Round Robin) scheduling algorithm.
 * Processes are executed in a cyclic order, each receiving a fixed time quantum for execution.
 * This approach ensures fair CPU allocation among processes.
 */
public class RR extends Scheduler {
    private int timeQuantum; // The time slice allocated to each process.

    /**
     * Constructor for RR Scheduler.
     *
     * @param processes   List of processes to schedule.
     * @param timeQuantum The fixed time quantum for each process.
     */
    public RR(List<Process> processes, int timeQuantum) {
        super(processes);
        this.timeQuantum = timeQuantum;
    }

    /**
     * Implements the Round Robin scheduling logic.
     * Each process gets a fixed time quantum for execution. If a process is not
     * finished within its time quantum, it is re-added to the queue for the next round.
     */
    @Override
    public void schedule() {
        Queue<Process> queue = new LinkedList<>(processes); // Queue to manage process execution.
        int currentTime = 0; // Tracks the current time in the system.

        // Initialize each process's remaining time with its burst time.
        processes.forEach(process -> process.setRemainingTime(process.getBurstTime()));

        while (!queue.isEmpty()) {
            Process process = queue.poll(); // Get the next process in the queue.

            // If the process arrives after the current time, CPU idles until its arrival.
            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime();
            }

            // Determine the time the process will execute in this round.
            int executionTime = Math.min(timeQuantum, process.getRemainingTime());

            // Set the start time of the process (only if it hasn't started yet).
            process.setStartTime(process.getStartTime() == 0 ? currentTime : process.getStartTime());

            // Update the current time after the process executes.
            currentTime += executionTime;

            // Update the process's remaining time.
            process.setRemainingTime(process.getRemainingTime() - executionTime);

            // If the process is not finished, re-add it to the queue for the next round.
            if (process.getRemainingTime() > 0) {
                queue.offer(process);
            } else {
                // If the process is finished, calculate its completion, turnaround, and waiting times.
                process.setCompletionTime(currentTime);
                process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
            }
        }
    }
}
