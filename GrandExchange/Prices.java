package core.GrandExchange;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Predicate;

public class Prices {
    private static HashMap<String, Item> items = new HashMap<>();

    public static void loadPrices() {
        JsonReader jsonReader = Json.createReader(new StringReader(getJson()));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        object.forEach((key, value) -> {
            JsonObject newObj = object.getJsonObject(key);

            if (!items.containsKey(newObj.getString("name"))) {
                items.put(newObj.getString("name"),
                        new Item(newObj.getInt("id"), newObj.getInt("sp"),
                                newObj.getInt("buy_average"), newObj.getInt("buy_quantity"),
                                newObj.getInt("sell_average"), newObj.getInt("sell_quantity"),
                                newObj.getInt("overall_average"), newObj.getInt("overall_quantity"),
                                newObj.getString("name"), newObj.getBoolean("members")));
            }
        });
    }

    public static HashMap<String, Item> getAllItems() {
        return items;
    }

    public static Collection<Item> getFilteredItems(Predicate<Item> predicate) {
        Collection<Item> list = new ArrayList<>();

        items.forEach((k, v) -> {
            if (Objects.nonNull(v)) {
                if (predicate.test(v)) {
                    list.add(v);
                }
            }
        });

        return list;
    }

    public static Item getItem(Predicate<Item> predicate) {
        Collection<Item> list = new ArrayList<>();

        items.forEach((k, v) -> {
            if (Objects.nonNull(v)) {
                if (predicate.test(v)) {
                    list.add(v);
                }
            }
        });

        return ((ArrayList<Item>) list).get(0);
    }

    private static String getJson() {
        String sURL = "https://rsbuddy.com/exchange/summary.json";

        try {
            InputStream is = new URL(sURL).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            return jsonText;
        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();

        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }
}
