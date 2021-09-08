package com.PayMyBuddy.MoneyTransfer.util;


public enum Currency {

    EUR(1.),
    USD(1.18),
    GBP(0.86);

    public final double value;

    Currency(double value) { this.value = value; }

}
