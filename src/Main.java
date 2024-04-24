import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/ffb4dee21964b41c0839950b/latest/";

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            displayMenu();
            option = scanner.nextInt();
            if (option != 0) {
                convertCurrency(option);
            }
        } while (option != 0);
    }

    private static void displayMenu() {
        System.out.println("Escolha uma opção: ");
        System.out.println("1 - Br para EUA");
        System.out.println("2 - Br para Euro");
        System.out.println("3 - EUA para Br");
        System.out.println("4 - EUA para Euro");
        System.out.println("5 - Euro para Br");
        System.out.println("6 - Euro para EUA");
        System.out.println("0 - Sair");
    }

    private static void convertCurrency(int option) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o valor em moeda de origem:");
        double amount = scanner.nextDouble();

        String fromCurrency, toCurrency;
        switch (option) {
            case 1:
                fromCurrency = "BRL";
                toCurrency = "USD";
                break;
            case 2:
                fromCurrency = "BRL";
                toCurrency = "EUR";
                break;
            case 3:
                fromCurrency = "USD";
                toCurrency = "BRL";
                break;
            case 4:
                fromCurrency = "USD";
                toCurrency = "EUR";
                break;
            case 5:
                fromCurrency = "EUR";
                toCurrency = "BRL";
                break;
            case 6:
                fromCurrency = "EUR";
                toCurrency = "USD";
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
                return;
        }

        double convertedAmount = getConvertedAmount(fromCurrency, toCurrency, amount);
        System.out.printf("%.2f %s = %.2f %s\n", amount, fromCurrency, convertedAmount, toCurrency);
    }

    private static double getConvertedAmount(String fromCurrency, String toCurrency, double amount) throws IOException, InterruptedException {
        String fullUrl = API_URL + fromCurrency;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        ResponseBody responseBody = gson.fromJson(response.body(), ResponseBody.class);

        JsonElement exchangeRate = responseBody.conversion_rates().get(toCurrency);

        return amount * exchangeRate.getAsDouble();
    }
}
