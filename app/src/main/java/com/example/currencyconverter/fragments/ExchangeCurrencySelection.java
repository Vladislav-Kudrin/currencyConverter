package com.example.currencyconverter.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.example.currencyconverter.R;

/**
 * Runs an exchange currency selection dialog fragment.
 *
 * @author Vladislav
 * @version 1.2
 * @since 1.0
 */
public class ExchangeCurrencySelection extends DialogFragment {
    /**
     * A user's currency selection.
     */
    private static int selection = 0;
    /**
     * A listener of a user's currency selection.
     */
    private static SelectionListener selectionListener;

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
}
