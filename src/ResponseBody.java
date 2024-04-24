import com.google.gson.JsonObject;

public record ResponseBody(
String result, JsonObject conversion_rates
) {
}
