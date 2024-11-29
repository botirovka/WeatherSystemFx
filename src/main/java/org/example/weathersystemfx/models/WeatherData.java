package org.example.weathersystemfx.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DecimalFormat;

public class WeatherData {
    private String stationName;
    private double temperature;
    private double humidity;
    private double windSpeed;
    DecimalFormat df = new DecimalFormat("#.##");

    public WeatherData(String stationName,double temperature, double humidity, double windSpeed) {
        this.stationName = stationName;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }
    public StringProperty stationNameProperty() {
        return new SimpleStringProperty(stationName);
    }

    public StringProperty temperatureProperty() {
        return new SimpleStringProperty(String.valueOf(df.format(temperature)));
    }

    public StringProperty humidityProperty() {
        return new SimpleStringProperty(String.valueOf(df.format(humidity)));
    }

    public StringProperty windSpeedProperty() {
        return new SimpleStringProperty(String.valueOf(df.format(windSpeed)));
    }

    @Override
    public String toString() {
        return String.format("Temperature: %.2f°C, Humidity: %.2f%%, Wind Speed: %.2f m/s",
                temperature, humidity, windSpeed);
    }
}
