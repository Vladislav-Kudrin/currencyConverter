package com.example.currencyconverter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Runs an exchange currency selection dialog fragment.
 *
 * @author Vladislav
 * @version 1.0
 * @since 1.0
 */
public final class ExchangeCurrencySelection extends DialogFragment {
    /**
     * A user's currency selection.
     */
    private static int selection = 0;
    /**
     * A listener of a user's currency selection.
     */
    static SelectionListener selectionListener;

    /**
     * Attaches {@code context} to {@code selectionListener}.
     *
     * @param context a caller's context.
     *
     * @author Vladislav
     * @since 1.0
     */
    @Override
    public final void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            selectionListener = (SelectionListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an exchange currency selection dialog fragment.
     *
     * @param savedInstanceState saved user's data.
     * @return the created exchange currency selection dialog fragment.
     *
     * @author Vladislav
     * @since 1.0
     */
    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] currencies = getResources().getStringArray(R.array.currencies);
        String[] signs = getResources().getStringArray(R.array.currencies_signs);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle(R.string.currency_selection).setSingleChoiceItems(currencies,
                selection, (dialog, item) ->
                        selection = item)
                .setPositiveButton(R.string.ok, (dialog, item) ->
                        selectionListener.onClickPositiveButton(currencies[selection],
                                signs[selection], selection))
                .setNegativeButton(R.string.cancel, (dialog, item) ->
                        selectionListener.onClickNegativeButton());

        return dialogBuilder.create();
    }

    /**
     * Listens user's currency selections.
     *
     * @author Vladislav
     * @version 1.0
     * @since 1.0
     */
    protected interface SelectionListener {
        /**
         * Performs actions if an exchange currency selection is confirmed.
         *
         * @param currency a selected exchange currency.
         * @param sign a selected exchange currency's sign.
         * @param selection a selected exchange currency's code.
         *
         * @author Vladislav
         * @since 1.0
         */
        void onClickPositiveButton(String currency, String sign, int selection);

        /**
         * Performs actions if an exchange currency selection is canceled.
         *
         * @author Vladislav
         * @since 1.0
         */
        void onClickNegativeButton();
    }
}
