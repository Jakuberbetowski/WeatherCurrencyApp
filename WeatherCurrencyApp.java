package zad2;

import javax.swing.*;
import java.awt.*;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WeatherCurrencyApp {
    private JFrame frame;
    private JTextField tfCity;
    private JTextField tfCountry;
    private JTextField tfCurrencyCode;
    private JButton btnFetch;
    private JTextArea taWeather;
    private JTextArea taExchange;
    private JTextArea taNBP;
    private JFXPanel jfxPanel;

    public void createAndShowGUI() {
        frame = new JFrame("Weather and Currency App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        frame.setContentPane(mainPanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(new JLabel("City:"));
        tfCity = new JTextField(10);
        inputPanel.add(tfCity);

        inputPanel.add(new JLabel("Country:"));
        tfCountry = new JTextField(15);
        inputPanel.add(tfCountry);

        inputPanel.add(new JLabel("Base Currency:"));
        tfCurrencyCode = new JTextField(4);
        inputPanel.add(tfCurrencyCode);

        btnFetch = new JButton("Fetch Data");
        inputPanel.add(btnFetch);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        taWeather = new JTextArea();
        taWeather.setLineWrap(true);
        taWeather.setWrapStyleWord(true);
        tabbedPane.addTab("Weather (JSON)", new JScrollPane(taWeather));

        taExchange = new JTextArea();
        tabbedPane.addTab("Exchange Rate", new JScrollPane(taExchange));

        taNBP = new JTextArea();
        tabbedPane.addTab("NBP Rate", new JScrollPane(taNBP));

        jfxPanel = new JFXPanel();
        jfxPanel.setPreferredSize(new Dimension(800, 300));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, jfxPanel);
        splitPane.setResizeWeight(0.5);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        btnFetch.addActionListener(e -> fetchData());

        frame.setVisible(true);
    }


    private void fetchData() {
        String city = tfCity.getText().trim();
        String country = tfCountry.getText().trim();
        String baseCurrency = tfCurrencyCode.getText().trim();

        if (city.isEmpty() || country.isEmpty() || baseCurrency.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields");
            return;
        }

        Service service = new Service(country);

        String weatherJson = service.getWeather(city);
        Double domesticRate = service.getRateFor(baseCurrency);
        Double plnRate = service.getNBPRate(baseCurrency);

        taWeather.setText(weatherJson);
        taExchange.setText(domesticRate != null ? domesticRate.toString() : "No Data");
        taNBP.setText(plnRate != null ? plnRate.toString() : "No Data");

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            String wikiUrl = "https://en.wikipedia.org/wiki/" + city;
            webEngine.load(wikiUrl);
            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });
    }
}
