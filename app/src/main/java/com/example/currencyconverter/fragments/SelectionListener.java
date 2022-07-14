package com.example.currencyconverter.fragments;

/**
 * Listens user's currency selections.
 *
 * @author Vladislav
 * @version 1.2
 * @since 1.0
 */
public interface SelectionListener {
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
