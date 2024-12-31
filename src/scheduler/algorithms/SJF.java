// File: scheduler/algorithms/SJF.java
package scheduler.algorithms;

import scheduler.models.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Shortest Job First (SJF) Scheduler implementation.
 * SJF is a non-preemptive scheduling algorithm where processes with the shortest burst time are executed first.
 */
public class SJF extends Scheduler {

    /**
     * Constructor for SJF Scheduler.
     *
     * @param processes List of processes to be scheduled.
     */
    public SJF(List<Process> processes) {
        super(processes);
    }

    /**
     * Implements the SJF scheduling algorithm (non-preemptive).
     * Processes are executed based on their burst time, considering their arrival times.
     */
    @Override
    public void schedule() {
        List<Process> scheduledProcesses = new ArrayList<>(); // Stores the scheduled processes in order.
        int currentTime = 0; // Tracks the current system time.

        while (!processes.isEmpty()) {
            // Step 1: Collect all processes that have arrived by the current time.
            List<Process> availableProcesses = new ArrayList<>();
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime) {
                    availableProcesses.add(process);
                }
            }

            // Step 2: If no process is available, increment time to handle idle state.
            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Step 3: Select the process with the shortest burst time among available processes.
            Process shortestJob = availableProcesses.stream()
                    .min(Comparator.comparingInt(Process::getBurstTime)) // Compare by burst time.
                    .orElseThrow(); // Safe as availableProcesses is not empty.

            // Remove the selected process from the original list.
            processes.remove(shortestJob);

            // Step 4: Handle any idle time before starting the process.
            if (currentTime < shortestJob.getArrivalTime()) {
                currentTime = shortestJob.getArrivalTime();
            }

            // Step 5: Compute and set scheduling metrics for the selected process.
            shortestJob.setStartTime(currentTime);
            shortestJob.setCompletionTime(currentTime + shortestJob.getBurstTime());
            shortestJob.setTurnaroundTime(shortestJob.getCompletionTime() - shortestJob.getArrivalTime());
            shortestJob.setWaitingTime(shortestJob.getTurnaroundTime() - shortestJob.getBurstTime());

            // Update the current time to the completion time of the process.
            currentTime = shortestJob.getCompletionTime();

            // Add the scheduled process to the final list.
            scheduledProcesses.add(shortestJob);
        }

        // Step 6: Restore the scheduled processes back to the original list.
        processes.addAll(scheduledProcesses);
    }
}
