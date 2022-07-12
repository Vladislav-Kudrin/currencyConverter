package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Runs the main application activity.
 *
 * @author Vladislav
 * @version 1.1
 * @since 1.0
 */
public final class MainActivity extends AppCompatActivity implements
        ExchangeCurrencySelection.SelectionListener {
    /**
     * An instance of a converter activity.
     */
    private static Intent converterActivity;
    /**
     * A modifiable set of currency/rate mappings.
     */
    private static JSONObject jsonObject;
    /**
     * A user's locale region.
     */
    private static Locale locale;
    /**
     * A current locale date.
     */
    private static String currentDate;
    /**
     * A JSON response from an exchange rates' API.
     */
    private static String json = "";
    /**
     * A selected exchange currency's code.
     */
    private static int exchangeCurrencyCode = 0;
    /**
     * A selected exchange currency.
     */
    private static String exchangeCurrency = "RUB";
    /**
     * A selected exchange currency's sign.
     */
    private static String exchangeCurrencySign = "₽";
    /**
     * An exchange currencies' rates' array.
     */
    private static final double[] RATES = new double[10];

    /**
     * Creates the main application activity.
     *
     * @param savedInstanceState saved user's data.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar;
        DateFormat dateFormat;
        final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        locale = Locale.getDefault();
        dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        converterActivity = new Intent(this, ConverterActivity.class);
        currentDate = dateFormat.format(Calendar.getInstance().getTime());

        ((Button) findViewById(R.id.dateButton)).setText(currentDate);

        if (savedInstanceState == null) {
            executorService.submit(new APIConnection());
            executorService.shutdown();
        }

        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar);
        }
    }

    /**
     * Restores saved user's data.
     *
     * @param savedInstanceState saved user's data.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    protected final void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        json = savedInstanceState.getString("JSON");
        currentDate = savedInstanceState.getString("CURRENT_DATE");
        exchangeCurrency = savedInstanceState.getString("EXCHANGE_CURRENCY");
        exchangeCurrencySign = savedInstanceState.getString("EXCHANGE_CURRENCY_SIGN");

        ((Button) findViewById(R.id.dateButton)).setText(currentDate);

        try {
            jsonObject = new JSONObject(json);

            getRates();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves user's data.
     *
     * @param outState saved user's data.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    protected final void onSaveInstanceState(Bundle outState) {
        outState.putString("JSON", json);
        outState.putString("CURRENT_DATE", currentDate);
        outState.putString("EXCHANGE_CURRENCY", exchangeCurrency);
        outState.putString("EXCHANGE_CURRENCY_SIGN", exchangeCurrencySign);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes {@code exchangeCurrency}, {@code exchangeCurrencyCode} and
     * {@code exchangeCurrencySign}.
     *
     * @param currency a selected exchange currency.
     * @param sign a selected exchange currency's sign.
     * @param selection a selected exchange currency's code.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    public final void onClickPositiveButton(String currency, String sign, int selection) {
        exchangeCurrency = currency;
        exchangeCurrencyCode = selection;
        exchangeCurrencySign = sign;

        getRates();
    }

    /**
     * @see ExchangeCurrencySelection.SelectionListener#onClickNegativeButton() .
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    public final void onClickNegativeButton() {}

    /**
     * Parses {@code jsonObject}.
     *
     * @author Vladislav
     * @since 1.0
     */
    private void getRates() {
        try {
            final double selectedRate = jsonObject.getJSONObject("conversion_rates")
                    .getDouble(exchangeCurrency);

            for (int index = 0; index < 10; index++) {
                RATES[index] = selectedRate / jsonObject.getJSONObject("conversion_rates")
                        .getDouble(getResources().getStringArray(R.array.currencies)[index]);
            }

            setRates();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Sets currency tiles values.
     *
     * @author Vladislav
     * @since 1.0
     */
    private void setRates() {
        int id;

        for (int index = 1; index < 10; index++) {
            id = getResources().getIdentifier("tile" + index, "id",
                    "com.example.currencyconverter");

            ((Button) findViewById(id)).setText(getSpan((index == exchangeCurrencyCode) ? 0 :
                    index), TextView.BufferType.SPANNABLE);
        }
    }

    /**
     * Formats a currency tile's title.
     *
     * @param index an index of the currency tile.
     * @return a formatted title of the currency tile.
     */
    private SpannableString getSpan(int index) {
        int length;
        final int start = 4;
        final int flags = 0;
        final SpannableString spannableString = new SpannableString(String.format(locale,
                getResources().getStringArray(R.array.currencies)[index] +
                        ((RATES[index] < 0.1) ? "\n%.4f" : "\n%.2f") +
                        exchangeCurrencySign, RATES[index]));

        length = spannableString.length();

        spannableString.setSpan(new AbsoluteSizeSpan(16, true), start, length, flags);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                start, length, flags);

        return spannableString;
    }

    /**
     * Opens a dialog fragment of a currency selection.
     *
     * @param view a user interface component.
     *
     * @author Vladislav
     * @since 1.0
     */
    public final void onClickMenuButton(View view) {
        final DialogFragment exchangeCurrencySelection = new ExchangeCurrencySelection();

        exchangeCurrencySelection.setCancelable(false);
        exchangeCurrencySelection.show(getSupportFragmentManager(),
                getResources().getString(R.string.currency_selection_title));
    }

    /**
     * Puts currency's data of a tapped currency tile to {@code converterActivity} and runs
     * {@code converterActivity}.
     *
     * @param view a user interface component.
     *
     * @author Vladislav
     * @since 1.0
     */
    public final void onClickCurrencyButton(View view) {
        converterActivity.putExtra("BUTTON_TEXT",
                String.valueOf(((Button) findViewById(view.getId())).getText()));
        converterActivity.putExtra("EXCHANGE_CURRENCY_SIGN", exchangeCurrencySign);

        startActivity(converterActivity);
    }

    /**
     * Gets and initializes default exchange currencies' rates or from an exchange rates API server.
     *
     * @author Vladislav
     * @version 1.1
     * @since 1.0
     */
    private final class APIConnection implements Runnable {
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
                jsonObject = new JSONObject("{\"conversion_rates\":{\"RUB\":1,\"USD\":1," +
                        "\"EUR\":1,\"JPY\":1,\"AUD\":1,\"AZN\":1,\"GBP\":1,\"AMD\":1,\"BYN\":1," +
                        "\"BGN\":1}}");

                getRates();
            } catch (JSONException exception) {
                exception.printStackTrace();
            }

            if (isOnline())
                apiConnect();
            else
                System.out.println("No internet access");
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
         * Initializes exchange rates from an exchange rates' API server if a response is received.
         *
         * @author Vladislav
         * @since 1.0
         */
        private void apiConnect() {
            try {
                String line;
                final URL url = new URL("https://v6.exchangerate-api.com/v6/" + APIConfig.CODE +
                        "/latest/" + exchangeCurrency);
                final URLConnection urlConnection = url.openConnection();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));

                while ((line = bufferedReader.readLine()) != null)
                    json = json.concat(line);

                bufferedReader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            runOnUiThread(() -> {
                if (json.length() != 0)
                    try {
                        jsonObject = new JSONObject(json);

                        getRates();
                    } catch (JSONException exception) {
                        exception.printStackTrace();
                    }
                else
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.failed_connection),
                            Toast.LENGTH_SHORT).show();
            });
        }
    }
}
