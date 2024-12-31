// File: scheduler/ui/GanttChart.java
package scheduler.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import scheduler.models.Process;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A class for creating a Gantt chart visualization of process scheduling data.
 */
public class GanttChart {

    private final List<Process> processes; // List of processes to display in the Gantt chart
    private final String baseColor;       // Base color for the Gantt chart visualization

    /**
     * Constructor for GanttChart.
     *
     * @param processes List of processes with scheduling details.
     * @param baseColor Base color for generating shades.
     */
    public GanttChart(List<Process> processes, String baseColor) {
        this.processes = processes;
        this.baseColor = baseColor;
    }

    /**
     * Creates the Gantt chart UI node with the chart, legend, table, and export button.
     *
     * @return A Node containing the Gantt chart and its components.
     */
    public Node getChartNode() {
        // Initialize axes for the chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Processes");
        xAxis.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: 'Segoe UI';");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Time");
        yAxis.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-font-family: 'Segoe UI';");

        // Create the stacked bar chart
        StackedBarChart<String, Number> barChart = new StackedBarChart<>(xAxis, yAxis);
        barChart.setTitle("Gantt Chart Visualization");
        barChart.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Roboto Slab'; -fx-font-style: italic;");
        barChart.setLegendVisible(false); // Disable default legend

        // Generate shades for each process
        String[] processColors = generateShades(baseColor, processes.size());

        // Populate the chart with data
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);

            // Calculate execution time
            int executionTime = process.getCompletionTime() - process.getStartTime();

            // Add data to chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            XYChart.Data<String, Number> data = new XYChart.Data<>(process.getName(), executionTime);

            // Add tooltip with process details
            Tooltip tooltip = new Tooltip(
                    "Process: " + process.getName() +
                            "\nBurst Time: " + process.getBurstTime() +
                            "\nArrival Time: " + process.getArrivalTime() +
                            "\nStart Time: " + process.getStartTime() +
                            "\nCompletion Time: " + process.getCompletionTime() +
                            "\nTurnaround Time: " + process.getTurnaroundTime()
            );
            Tooltip.install(data.getNode(), tooltip);

            // Apply color styling
            String color = processColors[i];
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });

            series.getData().add(data);
            barChart.getData().add(series);
        }

        // Create components: legend, process table, export button
        HBox legend = createLegend(processColors);
        GridPane processTable = createProcessTable();
        Button exportButton = createExportButton(barChart);

        // Combine components into a VBox
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(barChart, legend, processTable, exportButton);

        return vbox;
    }

    /**
     * Creates a legend showing process names and their corresponding colors.
     *
     * @param processColors Array of colors for each process.
     * @return An HBox containing the legend.
     */
    private HBox createLegend(String[] processColors) {
        HBox legend = new HBox();
        legend.setSpacing(15);
        legend.setAlignment(Pos.CENTER);

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);

            // Color box
            Label colorBox = new Label();
            colorBox.setPrefSize(20, 20);
            colorBox.setStyle("-fx-background-color: " + processColors[i] + "; -fx-border-color: black;");

            // Process label
            Label processLabel = new Label(process.getName());
            processLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            // Group color box and label
            HBox legendItem = new HBox(colorBox, processLabel);
            legendItem.setSpacing(5);
            legendItem.setAlignment(Pos.CENTER);

            legend.getChildren().add(legendItem);
        }

        return legend;
    }

    /**
     * Creates a table displaying detailed process information.
     *
     * @return A GridPane containing the process details.
     */
    private GridPane createProcessTable() {
        GridPane table = new GridPane();
        table.setHgap(15);
        table.setVgap(10);
        table.setAlignment(Pos.CENTER);

        // Table headings
        String[] headings = {"Process", "Burst Time", "Arrival Time", "Completion Time", "Turnaround Time"};
        for (int i = 0; i < headings.length; i++) {
            Text header = new Text(headings[i]);
            header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            table.add(header, i, 0);
        }

        // Table content
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);

            table.add(new Label(process.getName()), 0, i + 1);
            table.add(new Label(String.valueOf(process.getBurstTime())), 1, i + 1);
            table.add(new Label(String.valueOf(process.getArrivalTime())), 2, i + 1);
            table.add(new Label(String.valueOf(process.getCompletionTime())), 3, i + 1);
            table.add(new Label(String.valueOf(process.getTurnaroundTime())), 4, i + 1);
        }

        return table;
    }

    /**
     * Creates an export button for saving the chart as an image.
     *
     * @param barChart The bar chart to export.
     * @return A Button for exporting the chart.
     */
    private Button createExportButton(StackedBarChart<String, Number> barChart) {
        Button exportButton = new Button("Export Gantt Chart");
        exportButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        exportButton.setOnAction(event -> {
            WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
            File file = new File("GanttChart.png");

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("Gantt chart saved as GanttChart.png");
            } catch (IOException e) {
                System.err.println("Failed to save Gantt chart: " + e.getMessage());
            }
        });

        return exportButton;
    }

    /**
     * Generates unique color shades based on the base color.
     *
     * @param baseColor Base color in hex format.
     * @param count     Number of shades to generate.
     * @return An array of hex color shades.
     */
    private String[] generateShades(String baseColor, int count) {
        String[] shades = new String[count];
        for (int i = 0; i < count; i++) {
            int adjustment = (i + 1) * 20;
            shades[i] = adjustColorBrightness(baseColor, adjustment);
        }
        return shades;
    }

    /**
     * Adjusts the brightness of a hex color.
     *
     * @param hexColor   Hex color code.
     * @param adjustment Brightness adjustment value.
     * @return Adjusted hex color code.
     */
    private String adjustColorBrightness(String hexColor, int adjustment) {
        int r = Integer.parseInt(hexColor.substring(1, 3), 16) + adjustment;
        int g = Integer.parseInt(hexColor.substring(3, 5), 16) + adjustment;
        int b = Integer.parseInt(hexColor.substring(5, 7), 16) + adjustment;

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return String.format("#%02x%02x%02x", r, g, b);
    }
}
