// File: scheduler/SchedulerMain.java

package scheduler;

import scheduler.algorithms.FCFS;
import scheduler.algorithms.PriorityScheduler;
import scheduler.algorithms.RR;
import scheduler.algorithms.SJF;
import scheduler.algorithms.Scheduler;
import scheduler.models.Process;
import scheduler.ui.GanttChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the Multi-Algorithm Task Scheduler.
 * This class creates a GUI to visualize multiple scheduling algorithms
 * (FCFS, SJF, Round Robin, Priority) using Gantt charts.
 */
public class SchedulerMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set the application window title
        primaryStage.setTitle("Multi-Algorithm Task Scheduler");

        // Initialize the list of processes
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 5, 2)); // Process(name, arrivalTime, burstTime, priority)
        processes.add(new Process("P2", 2, 3, 1));
        processes.add(new Process("P3", 4, 2, 3));
        processes.add(new Process("P4", 6, 4, 2)); // Additional process
        processes.add(new Process("P5", 8, 6, 1)); // Additional process

        // Create a TabPane to hold different algorithm visualizations
        TabPane tabPane = new TabPane();

        // Add each scheduling algorithm as a separate tab
        tabPane.getTabs().add(createGanttTab("FCFS", new FCFS(new ArrayList<>(processes)), "#6A0DAD")); // Purple
        tabPane.getTabs().add(createGanttTab("SJF", new SJF(new ArrayList<>(processes)), "#FFD700")); // Yellow
        tabPane.getTabs().add(createGanttTab("Round Robin", new RR(new ArrayList<>(processes), 2), "#32CD32")); // Green
        tabPane.getTabs().add(createGanttTab("Priority", new PriorityScheduler(new ArrayList<>(processes)), "#FF4500")); // Red

        // Set up the main scene and display it
        Scene scene = new Scene(tabPane, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a Tab for a specific scheduling algorithm and its Gantt chart.
     *
     * @param title The title of the Tab (e.g., "FCFS", "SJF").
     * @param scheduler The scheduling algorithm instance.
     * @param baseColor The base color for the Gantt chart visualization.
     * @return A Tab containing the Gantt chart for the algorithm.
     */
    private Tab createGanttTab(String title, Scheduler scheduler, String baseColor) {
        // Run the scheduling algorithm
        scheduler.schedule();

        // Print process details to the console
        printProcessDetails(title, scheduler.getProcesses());

        // Create a Gantt chart for the scheduled processes
        GanttChart ganttChart = new GanttChart(scheduler.getProcesses(), baseColor);

        // Create and configure the Tab
        Tab tab = new Tab(title, ganttChart.getChartNode());
        tab.setClosable(false); // Prevent the user from closing the Tab
        return tab;
    }

    /**
     * Prints the details of scheduled processes to the console for debugging or review.
     *
     * @param algorithmName The name of the scheduling algorithm.
     * @param processes The list of scheduled processes.
     */
    private void printProcessDetails(String algorithmName, List<Process> processes) {
        System.out.println("=== " + algorithmName + " ===");
        System.out.printf("%-10s %-15s %-15s %-15s %-15s%n",
                "Process", "Burst Time", "Arrival Time", "Completion Time", "Turnaround Time");
        for (Process process : processes) {
            System.out.printf("%-10s %-15d %-15d %-15d %-15d%n",
                    process.getName(),
                    process.getBurstTime(),
                    process.getArrivalTime(),
                    process.getCompletionTime(),
                    process.getTurnaroundTime());
        }
        System.out.println();
    }

    /**
     * Main method to launch the application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
