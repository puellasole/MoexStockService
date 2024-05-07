package ru.dasha.MoexService.dto;

import java.util.List;

public class TickersDto {
    private List<String> tickers;

    public TickersDto() {};

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }
}