// File: scheduler/algorithms/PriorityScheduler.java
package scheduler.algorithms;

import scheduler.models.Process;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Priority Scheduler implementation.
 * Processes are executed based on their priority, where a lower priority value indicates higher priority.
 * Non-preemptive scheduling is applied.
 */
public class PriorityScheduler extends Scheduler {

    /**
     * Constructor for PriorityScheduler.
     *
     * @param processes List of processes to schedule.
     */
    public PriorityScheduler(List<Process> processes) {
        super(processes);
    }

    /**
     * Implements the Priority Scheduling algorithm (non-preemptive).
     * Processes are executed in the order of their priority, considering their arrival times.
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

            // Step 2: If no process has arrived, increment time (idle state).
            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Step 3: Select the highest-priority process from the available processes.
            Process highestPriorityProcess = availableProcesses.stream()
                    .min(Comparator.comparingInt(Process::getPriority)) // Lower priority value = higher priority.
                    .orElseThrow(); // Safe to use as availableProcesses is not empty.

            // Remove the selected process from the list of unscheduled processes.
            processes.remove(highestPriorityProcess);

            // Step 4: Handle any idle time before starting the process.
            if (currentTime < highestPriorityProcess.getArrivalTime()) {
                currentTime = highestPriorityProcess.getArrivalTime();
            }

            // Step 5: Compute and set scheduling attributes for the selected process.
            highestPriorityProcess.setStartTime(currentTime);
            highestPriorityProcess.setCompletionTime(currentTime + highestPriorityProcess.getBurstTime());
            highestPriorityProcess.setTurnaroundTime(highestPriorityProcess.getCompletionTime() - highestPriorityProcess.getArrivalTime());
            highestPriorityProcess.setWaitingTime(highestPriorityProcess.getTurnaroundTime() - highestPriorityProcess.getBurstTime());

            // Update the current time to reflect the process completion.
            currentTime = highestPriorityProcess.getCompletionTime();

            // Add the scheduled process to the final list.
            scheduledProcesses.add(highestPriorityProcess);
        }

        // Step 6: Restore processes back to the original list for consistency.
        processes.addAll(scheduledProcesses);
    }
}
