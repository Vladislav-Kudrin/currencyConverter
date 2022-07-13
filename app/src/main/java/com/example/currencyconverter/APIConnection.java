package com.example.currencyconverter;

import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

/**
 * Gets and initializes default exchange currencies' rates or from an exchange rates API server.
 *
 * @author Vladislav
 * @version 1.2
 * @since 1.0
 */
final class APIConnection implements Runnable {
    /**
     * A modifiable set of currency/rate mappings from a submitter main activity class.
     */
    private static JSONObject jsonObject;
    /**
     * A modifiable set of currency/rate mappings from.
     */
    private static JSONObject conversionRates;
    /**
     * Submitter main activity class.
     */
    private static MainActivity mainActivity;
    /**
     * A JSON response from an exchange rates API server.
     */
    private static String json;
    /**
     * {@code jsonObject} key name.
     */
    private static final String CONVERSION_RATES = "conversion_rates";

    public APIConnection(MainActivity mainActivity, JSONObject jsonObject) {
        APIConnection.mainActivity = mainActivity;
        APIConnection.jsonObject = jsonObject;
    }

    /**
     * Initializes default exchange currencies' rates and tries to connect to an exchange rates
     * API server.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    public final void run() {
        try {
            conversionRates = new JSONObject("{\"RUB\":1,\"USD\":1," +
                    "\"EUR\":1,\"JPY\":1,\"AUD\":1,\"AZN\":1,\"GBP\":1,\"AMD\":1,\"BYN\":1," +
                    "\"BGN\":1}}");
            jsonObject.put(CONVERSION_RATES, conversionRates);

            mainActivity.getRates();
        } catch (JSONException exception) {
            exception.printStackTrace();
        }

        if (isOnline())
            apiConnect();

        updateUi();
    }

    /**
     * Checks for internet access.
     * Returns true if there is internet access and false otherwise.
     *
     * @return internet access availability.
     *
     * @author Vladislav
     * @since 1.1
     */
    private boolean isOnline() {
        try {
            final Socket socket = new Socket();
            final SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);

            socket.connect(socketAddress, 1500);
            socket.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Initializes {@code json} with exchange rates from an exchange rates' API server if a
     * response is received.
     *
     * @author Vladislav
     * @since 1.0
     */
    private void apiConnect() {
        try {
            String line;
            final URL url = new URL("https://v6.exchangerate-api.com/v6/" + KeysStorage.API_KEY +
                    "/latest/RUB");
            final URLConnection urlConnection = url.openConnection();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            json = "";

            while ((line = bufferedReader.readLine()) != null)
                json = json.concat(line);

            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Updates exchange currencies' tiles' values.
     *
     * @author Vladislav
     * @since 1.1
     */
    private void updateUi() {
        mainActivity.runOnUiThread(() -> {
            if (json == null)
                Toast.makeText(mainActivity.getApplicationContext(),
                        mainActivity.getResources().getString(R.string.no_internet_access),
                        Toast.LENGTH_SHORT).show();
            else if (json.length() != 0)
                try {
                    conversionRates = new JSONObject(json);

                    jsonObject.put(CONVERSION_RATES,
                            conversionRates.getJSONObject(CONVERSION_RATES));
                    mainActivity.getRates();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            else
                Toast.makeText(mainActivity.getApplicationContext(),
                        mainActivity.getResources().getString(R.string.failed_connection),
                        Toast.LENGTH_SHORT).show();
        });
    }
}
