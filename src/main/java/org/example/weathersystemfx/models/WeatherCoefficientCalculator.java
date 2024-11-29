package org.example.weathersystemfx.models;

public class WeatherCoefficientCalculator {
    static double snowCoefficient = 0;
    static double stormCoefficient = 0;
    static double rainCoefficient = 0;
    static double fogCoefficient = 0;
    static double thunderstormCoefficient = 0;
    static double heatwaveCoefficient = 0;

    public static WeatherForecastCoefficients calculateCoefficients(WeatherData data) {

        // Коефіцієнт для снігу
        if (data.getHumidity() > 80 && data.getTemperature() < 0) {
            snowCoefficient += 0.1;
        }

        // Коефіцієнт для шторму
        if (data.getWindSpeed() > 15 && data.getHumidity() > 70) {
            stormCoefficient += 0.1;
        }

        // Коефіцієнт для дощу
        if (data.getTemperature() > 20 && data.getHumidity() > 60) {
            rainCoefficient += 0.1;
        }

        // Коефіцієнт для туману
        if (data.getHumidity() > 90 && data.getWindSpeed() < 5) {
            fogCoefficient += 0.1;
        }

        // Коефіцієнт для грози
        if (data.getWindSpeed() > 20 && data.getHumidity() > 80 && data.getTemperature() > 15) {
            thunderstormCoefficient += 0.1;
        }

        // Коефіцієнт для спеки
        if (data.getTemperature() > 30) {
            heatwaveCoefficient += 0.1;
        }

        return new WeatherForecastCoefficients(snowCoefficient, stormCoefficient, rainCoefficient, fogCoefficient, thunderstormCoefficient, heatwaveCoefficient);
    }
}
