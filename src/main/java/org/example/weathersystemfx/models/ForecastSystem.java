package org.example.weathersystemfx.models;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ForecastSystem {
    private BlockingQueue<WeatherData> forecastQueue;
    private double totalTemperature = 0;
    private double totalHumidity = 0;
    private double totalWindSpeed = 0;
    private int count = 0;  // Кількість отриманих даних
    private final Object lock = new Object();  // Лок для синхронізації доступу
    private TableView<WeatherData> stationsTable;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> tempSeries;
    private XYChart.Series<Number, Number> humiditySeries;
    private XYChart.Series<Number, Number> windSpeedSeries;
    private int time = 0;// Для оновлення таблиці
    private Label forecastLabel;

    public ForecastSystem(TableView<WeatherData> stationsTable, LineChart<Number, Number> lineChart, Label forecastLabel) {
        this.forecastQueue = new LinkedBlockingQueue<>();
        this.stationsTable = stationsTable;
        this.lineChart = lineChart;
        this.forecastLabel = forecastLabel;

        Platform.runLater(() -> {
            tempSeries = new XYChart.Series<>();
            tempSeries.setName("Temperature");

            humiditySeries = new XYChart.Series<>();
            humiditySeries.setName("Humidity");

            windSpeedSeries = new XYChart.Series<>();
            windSpeedSeries.setName("Wind Speed");

            lineChart.getData().addAll(tempSeries, humiditySeries, windSpeedSeries);
        });
    }

    public void receiveForecastData(WeatherData data) {
        try {
            System.out.println("Отримані дані: " + data);
            System.out.println("Поточні значення:");
            System.out.printf("Температура: %.2f\n", data.getTemperature());
            System.out.printf("Вологість: %.2f\n", data.getHumidity());
            System.out.printf("Швидкість вітру: %.2f\n", data.getWindSpeed());
            forecastQueue.put(data);
            System.out.println("Головна система отримала дані: " + data);

            synchronized (lock) {
                totalTemperature += data.getTemperature();
                totalHumidity += data.getHumidity();
                totalWindSpeed += data.getWindSpeed();
                count++;
            }

            double avgTemp = getAverageTemperature();
            double avgHumidity = getAverageHumidity();
            double avgWindSpeed = getAverageWindSpeed();

            //Коефіцієнти
            WeatherForecastCoefficients coefficients = WeatherCoefficientCalculator.calculateCoefficients(data);


            Platform.runLater(() -> {
                time++;
                tempSeries.getData().add(new XYChart.Data<>(time, avgTemp));
                humiditySeries.getData().add(new XYChart.Data<>(time, avgHumidity));
                windSpeedSeries.getData().add(new XYChart.Data<>(time, avgWindSpeed));
                forecastLabel.setText("Загальний прогноз: " + coefficients.getWeatherForecast());
            });


            Platform.runLater(() -> stationsTable.getItems().add(data));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Помилка при передачі даних в головну систему.");
        }
    }

    public double getAverageTemperature() {
        synchronized (lock) {
            return count == 0 ? 0 : totalTemperature / count;
        }
    }

    public double getAverageHumidity() {
        synchronized (lock) {
            return count == 0 ? 0 : totalHumidity / count;
        }
    }

    public double getAverageWindSpeed() {
        synchronized (lock) {
            return count == 0 ? 0 : totalWindSpeed / count;
        }
    }

    public void processForecast() {
        try {
            while (true) {
                WeatherData data = forecastQueue.take();
                System.out.println("Головна система обробляє прогноз на основі даних: " + data);
                System.out.printf("Середня температура: %.2f°C\n", getAverageTemperature());
            }
        } catch (InterruptedException e) {
            System.out.println("Прогнозування перервано.");
            Thread.currentThread().interrupt();
        }
    }
}