package zad2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Service {
    private String countryName;

    public Service(String countryName) {
        this.countryName = countryName;
    }


    private String convertCountryNameToCode() {
        String name = countryName.trim().toLowerCase();
        switch (name) {
            case "poland":
                return "PL";
            case "united states":
                return "US";
            case "united kingdom":
                return "GB";
            case "japan":
                return "JP";
            case "germany":
                return "DE";
            case "france":
                return "FR";
            case "italy":
                return "IT";
            case "spain":
                return "ES";
            default:
                return "XX";
        }
    }


    private String getDomesticCurrency() {
        String name = countryName.trim().toLowerCase();
        switch (name) {
            case "poland":
                return "PLN";
            case "united states":
                return "USD";
            case "united kingdom":
                return "GBP";
            case "japan":
                return "JPY";
            case "germany":
            case "france":
            case "italy":
            case "spain":
                return "EUR";
            default:
                return "EUR";
        }
    }


    public String getWeather(String city) {
        try {
            String apiKey = "35c748d6dc0b5bc21a49e0c4979a9d4a";
            String countryCode = convertCountryNameToCode();
            String urlStr = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "," + countryCode + "&appid=" + apiKey;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Wystąpił problem z pobraniem danych pogodowych\"}";
        }
    }

    public Double getRateFor(String baseCurrency) {
        try {
            String urlStr = "https://open.er-api.com/v6/latest/" + baseCurrency;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseText = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseText.append(inputLine);
            }
            in.close();
            conn.disconnect();
            String response = responseText.toString();

            String domesticCurrency = getDomesticCurrency();
            String searchKey = "\"" + domesticCurrency + "\":";
            int index = response.indexOf(searchKey);
            if (index == -1) {
                return null;
            }
            int start = index + searchKey.length();
            int end = response.indexOf(",", start);
            if (end == -1) {
                end = response.indexOf("}", start);
            }
            if (start < end) {
                String rateStr = response.substring(start, end).trim();
                return Double.parseDouble(rateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double getNBPRate(String baseCurrency) {
        try {
            String urlStr = "https://open.er-api.com/v6/latest/" + baseCurrency;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseText = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseText.append(inputLine);
            }
            in.close();
            conn.disconnect();
            String response = responseText.toString();

            String searchKey = "\"PLN\":";
            int index = response.indexOf(searchKey);
            if (index == -1) {
                return null;
            }
            int start = index + searchKey.length();
            int end = response.indexOf(",", start);
            if (end == -1) {
                end = response.indexOf("}", start);
            }
            if (start < end) {
                String rateStr = response.substring(start, end).trim();
                return Double.parseDouble(rateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
