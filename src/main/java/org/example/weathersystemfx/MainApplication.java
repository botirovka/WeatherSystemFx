package org.example.weathersystemfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.weathersystemfx.models.*;

import java.util.concurrent.*;

public class MainApplication extends Application {
    private TableView<WeatherData> stationsTable;
    private LineChart<Number, Number> lineChart;
    private TextField stationCountField;
    private TextField durationField;
    private Button startButton;
    private Label forecastLabel;
    private ExecutorService executorService;
    private BlockingQueue<WeatherData> dataQueue;
    private ForecastSystem forecastSystem;
    private int stationCounter;

    @Override
    public void start(Stage primaryStage) {
        // Ініціалізація компонентів
        stationsTable = new TableView<>();
        TableColumn<WeatherData, String> stationNameColumn = new TableColumn<>("Station Name");
        TableColumn<WeatherData, String> temperatureColumn = new TableColumn<>("Temperature");
        TableColumn<WeatherData, String> humidityColumn = new TableColumn<>("Humidity");
        TableColumn<WeatherData, String> windSpeedColumn = new TableColumn<>("Wind Speed");

        stationNameColumn.setCellValueFactory(cellData -> cellData.getValue().stationNameProperty());
        temperatureColumn.setCellValueFactory(cellData -> cellData.getValue().temperatureProperty());
        humidityColumn.setCellValueFactory(cellData -> cellData.getValue().humidityProperty());
        windSpeedColumn.setCellValueFactory(cellData -> cellData.getValue().windSpeedProperty());

        stationsTable.getColumns().addAll(stationNameColumn, temperatureColumn, humidityColumn, windSpeedColumn);



        // Графік
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Weather Trends");

        stationCountField = new TextField();
        stationCountField.setPromptText("Number of Stations");

        durationField = new TextField();
        durationField.setPromptText("Duration (seconds)");

        startButton = new Button("Start Simulation");
        startButton.setOnAction(event -> startSimulation());
        forecastLabel = new Label("Загальний прогноз: Сприятливі погодні умови");

        Button addStationButton = new Button("Add Station");
        addStationButton.setOnAction(event -> addNewStation());

        HBox inputBox = new HBox(10, new Label("Stations:"), stationCountField, new Label("Duration:"), durationField, startButton, addStationButton);
        inputBox.setPadding(new Insets(10));

        VBox root = new VBox(10, inputBox, stationsTable, forecastLabel, lineChart);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather Simulation");
        primaryStage.show();
    }

    private void startSimulation() {
        try {
            int stationCount = Integer.parseInt(stationCountField.getText());
            int duration = Integer.parseInt(durationField.getText());

            Task<Void> simulationTask = new Task<>() {
                @Override
                protected Void call() {
                    dataQueue = new LinkedBlockingQueue<>();
                    forecastSystem = new ForecastSystem(stationsTable, lineChart, forecastLabel);

                    executorService = new ThreadPoolExecutor(
                            stationCount + 2,
                            50, //обмеження максимальної кількості потоків
                            60L, TimeUnit.SECONDS,
                            new SynchronousQueue<>()
                    );


                    for (int i = 1; i <= stationCount; i++) {
                        WeatherStation station = new WeatherStation("Station " + i, dataQueue);
                        executorService.submit(station);
                        stationCounter++;
                    }

                    for (int i = 0; i < 2; i++) {
                        DataProcessor dataProcessor = new DataProcessor(dataQueue, forecastSystem);
                        executorService.submit(dataProcessor);
                    }

                    Thread forecastThread = new Thread(forecastSystem::processForecast);
                    forecastThread.start();

                    try {
                        Thread.sleep(duration * 1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    executorService.shutdownNow();
                    forecastThread.interrupt();

                    return null;
                }
            };

            new Thread(simulationTask).start();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers for stations and duration.");
        }
    }

    private void addNewStation() {
        if (executorService != null && !executorService.isShutdown()) {
            int stationNumber = stationCounter+1;
            stationCounter++;
            WeatherStation newStation = new WeatherStation("Station " + stationNumber, dataQueue);
            executorService.submit(newStation);

            Platform.runLater(() -> {
                showAlert("New Station Added", "Station " + stationNumber + " has been added to the simulation.");
            });
        } else {
            showAlert("Simulation Not Running", "Start the simulation before adding new stations.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
