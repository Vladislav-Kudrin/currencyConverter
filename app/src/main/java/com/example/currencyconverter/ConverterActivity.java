package com.example.currencyconverter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Locale;

/**
 * Runs a converter activity.
 *
 * @author Vladislav
 * @version 1.2
 * @since 1.0
 */
public final class ConverterActivity extends AppCompatActivity {
    /**
     * An exchangeable currency rate.
     */
    private double rate;
    /**
     * An initial value of a conversion result.
     */
    private String initialConverted;

    /**
     * Creates a converter activity.
     *
     * @param savedInstanceState saved user's data.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        ActionBar actionBar;
        final String full = getIntent().getExtras().getString(KeysStorage.BUTTON_TEXT);
        final String sign = getIntent().getExtras().getString(KeysStorage.EXCHANGE_CURRENCY_SIGN);
        final String currency = full.substring(0, 3);
        final String rateString = full.substring(4);
        final EditText amount = findViewById(R.id.amount);
        final TextView converted = findViewById(R.id.converted);

        rate = Double.parseDouble(rateString.replace(',', '.').replaceAll("[^.0-9]", ""));
        initialConverted = getResources().getString(R.string.initial).concat(sign);

        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_currency);
            ((TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title))
                    .setText(currency);
        }

        converted.setText(initialConverted);
        ((TextView) findViewById(R.id.rateNumber)).setText(rateString);
        ((TextView) findViewById(R.id.currency)).setText(currency);
        ((TextView) findViewById(R.id.currencyName)).setText(getResources()
                .getStringArray(R.array.currencies_names)[getCurrencyCode(currency)]);
        amount.addTextChangedListener(new TextWatcher() {
            /**
             * @see android.text.TextWatcher#beforeTextChanged(CharSequence, int, int, int) .
             *
             * @param charSequence a changed amount of an exchangeable currency.
             * @param start a beginning character of a replaced part of {@code charSequence}.
             * @param count an old length of characters of a replaced part of {@code charSequence}.
             * @param after a new length of a replaced part of {@code charSequence}.
             *
             * @author Vladislav
             * @since 1.0
             */
            @Override
            public final void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {}

            /**
             * @see android.text.TextWatcher#afterTextChanged(Editable) .
             *
             * @param editable a changed amount of an exchangeable currency.
             *
             * @author Vladislav
             * @since 1.0
             */
            @Override
            public final void afterTextChanged(Editable editable) {}

            /**
             * Formats a conversion result.
             *
             * @param charSequence a changed amount of an exchangeable currency.
             * @param start a beginning character of a replaced part of {@code charSequence}.
             * @param before an old length of characters of a replaced part of {@code charSequence}.
             * @param count a new length of a replaced part of {@code charSequence}.
             *
             * @author Vladislav
             * @since 1.0
             */
            @Override
            public final void onTextChanged(CharSequence charSequence, int start, int before,
                                            int count) {
                double result;
                int charSequenceLength = charSequence.length();
                String amountString = charSequence.toString();
                final int minLength = 0;

                if (charSequenceLength == minLength || amountString.equals("."))
                    converted.setText(initialConverted);
                else if (!amountString.matches("(^\\d{0,11}\\.\\d{0,2}$)|(^\\d{0,12}$)")) {
                    charSequenceLength = charSequenceLength - 1;

                    amount.setText(amountString.substring(minLength, charSequenceLength));
                    amount.setSelection(charSequenceLength);
                } else {
                    result = Double.parseDouble(amountString) * rate;

                    if (result == Math.floor(result))
                        amountString = String.format(Locale.getDefault(), "%.0f", result);
                    else
                        amountString = String.format(Locale.getDefault(), (result < 0.1) ? "%.6f" :
                                "%.2f", result).replaceAll("(?!^)0+$", "");

                    converted.setText(amountString.concat(sign));
                }
            }
        });
    }

    /**
     * Defines a currency code.
     *
     * @param currency an international currency code.
     * @return the currency code.
     *
     * @author Vladislav
     * @since 1.0
     */
    private int getCurrencyCode(String currency) {
        switch (currency) {
            case "RUB":
                return 0;
            case "USD":
                return 1;
            case "EUR":
                return 2;
            case "JPY":
                return 3;
            case "AUD":
                return 4;
            case "AZN":
                return 5;
            case "GBP":
                return 6;
            case "AMD":
                return 7;
            case "BYN":
                return 8;
            case "BGN":
                return 9;
            case "KZT":
                return 10;
            case "CNY":
                return 11;
            default:
                return 12;
        }
    }

    /**
     * Destroys a converter activity.
     *
     * @param view a user interface component.
     *
     * @author Vladislav
     * @since 1.0
     */
    public final void onClickBackButton(View view) {
        finish();
    }
}
