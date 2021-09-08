package com.PayMyBuddy.MoneyTransfer.util;

public class CurrencyConverter {

    public static double convert(String from, String to, double amount){
        return (amount / Currency.valueOf(from).value) * Currency.valueOf(to).value;
    }

}
