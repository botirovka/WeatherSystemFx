package org.example.weathersystemfx.models;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class WeatherStation implements Runnable {
    private String stationName;
    private BlockingQueue<WeatherData> dataQueue;

    public WeatherStation(String stationName, BlockingQueue<WeatherData> dataQueue) {
        this.stationName = stationName;
        this.dataQueue = dataQueue;
    }

    private WeatherData generateData() {
        Random random = new Random();
        double temperature = -10 + random.nextDouble() * 40; // Температура: від -10 до 30
        double humidity = 10 + random.nextDouble() * 90; // Вологість: від 10% до 100%
        double windSpeed = random.nextDouble() * 20; // Швидкість вітру: від 0 до 20 м/с

        return new WeatherData(this.stationName,temperature, humidity, windSpeed);
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                WeatherData data = generateData();
                System.out.printf("Station %s generated data: %s\n", stationName, data);
                dataQueue.put(data);  // Поміщаємо дані у чергу для обробки
                Thread.sleep(2000);  // Затримка на 2 секунди перед наступним збором
            }
        } catch (InterruptedException e) {
            System.out.println("Weather station interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}