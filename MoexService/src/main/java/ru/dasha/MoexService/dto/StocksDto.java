package ru.dasha.MoexService.dto;

import java.util.List;

import ru.dasha.MoexService.model.Stock;

public class StocksDto {
    private List<Stock> stocks;

    public StocksDto(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
