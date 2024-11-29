package org.example.weathersystemfx.models;

import java.util.concurrent.BlockingQueue;

public class DataProcessor implements Runnable {
    private BlockingQueue<WeatherData> dataQueue;
    private ForecastSystem forecastSystem;

    public DataProcessor(BlockingQueue<WeatherData> dataQueue, ForecastSystem forecastSystem) {
        this.dataQueue = dataQueue;
        this.forecastSystem = forecastSystem;
    }

    private void analyzeData(WeatherData data) {
        System.out.println("Analyzing data: " + data);
        forecastSystem.receiveForecastData(data);
    }

    @Override
    public void run() {
        try {
            while (true) {
                WeatherData data = dataQueue.take();  // Забираємо дані з черги
                analyzeData(data);  // Обробка даних
            }
        } catch (InterruptedException e) {
            System.out.println("Data processing interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}
