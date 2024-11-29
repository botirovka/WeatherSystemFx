package org.example.weathersystemfx.models;

public class WeatherForecastCoefficients {
    private double snowCoefficient;
    private double stormCoefficient;
    private double rainCoefficient;
    private double fogCoefficient;
    private double thunderstormCoefficient;
    private double heatwaveCoefficient;

    public WeatherForecastCoefficients(double snowCoefficient, double stormCoefficient, double rainCoefficient,
                                       double fogCoefficient, double thunderstormCoefficient, double heatwaveCoefficient) {
        this.snowCoefficient = snowCoefficient;
        this.stormCoefficient = stormCoefficient;
        this.rainCoefficient = rainCoefficient;
        this.fogCoefficient = fogCoefficient;
        this.thunderstormCoefficient = thunderstormCoefficient;
        this.heatwaveCoefficient = heatwaveCoefficient;
    }

    // Гетери для коефіцієнтів
    public double getSnowCoefficient() {
        return snowCoefficient;
    }

    public double getStormCoefficient() {
        return stormCoefficient;
    }

    public double getRainCoefficient() {
        return rainCoefficient;
    }

    public double getFogCoefficient() {
        return fogCoefficient;
    }

    public double getThunderstormCoefficient() {
        return thunderstormCoefficient;
    }

    public double getHeatwaveCoefficient() {
        return heatwaveCoefficient;
    }

    public double getMaxCoefficient() {
        return Math.max(Math.max(Math.max(snowCoefficient, stormCoefficient), Math.max(rainCoefficient, fogCoefficient)),
                Math.max(thunderstormCoefficient, heatwaveCoefficient));
    }
    public static String getCoefficientType(WeatherForecastCoefficients coefficients) {
        if (coefficients.getMaxCoefficient() == coefficients.getSnowCoefficient()) {
            return "Snow";
        } else if (coefficients.getMaxCoefficient() == coefficients.getStormCoefficient()) {
            return "Storm";
        } else if (coefficients.getMaxCoefficient() == coefficients.getRainCoefficient()) {
            return "Rain";
        } else if (coefficients.getMaxCoefficient() == coefficients.getFogCoefficient()) {
            return "Fog";
        } else if (coefficients.getMaxCoefficient() == coefficients.getThunderstormCoefficient()) {
            return "Thunderstorm";
        } else if (coefficients.getMaxCoefficient() == coefficients.getHeatwaveCoefficient()) {
            return "Heatwave";
        }
        return "Unknown";
    }


    public String getWeatherForecast() {
        double maxCoefficient = getMaxCoefficient();
        if (maxCoefficient == snowCoefficient) {
            return "Прогноз: Сніг";
        } else if (maxCoefficient == stormCoefficient) {
            return "Прогноз: Шторм";
        } else if (maxCoefficient == rainCoefficient) {
            return "Прогноз: Дощ";
        } else if (maxCoefficient == fogCoefficient) {
            return "Прогноз: Туман";
        } else if (maxCoefficient == thunderstormCoefficient) {
            return "Прогноз: Гроза";
        } else if (maxCoefficient == heatwaveCoefficient) {
            return "Прогноз: Спека";
        } else {
            return "Прогноз: Невизначено";
        }
    }
}
