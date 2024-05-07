package ru.dasha.MoexService.model;

public class Stock {
    private String ticker;
    private String figi;
    private String name;
    private String type;
    private Currency currency;
    private String source;

    private Stock(Builder builder) {
        this.ticker = builder.ticker;
        this.figi = builder.figi;
        this.name = builder.name;
        this.type = builder.type;
        this.currency = builder.currency;
        this.source = builder.source;
    }

    public String getTicker() {
        return ticker;
    }

    public String getFigi() {
        return figi;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getSource() {
        return source;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String ticker;
        private String figi;
        private String name;
        private String type;
        private Currency currency;
        private String source;

        public Builder ticker(String ticker) {
            this.ticker = ticker;
            return this;
        }

        public Builder figi(String figi) {
            this.figi = figi;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Stock build() {
            return new Stock(this);
        }
    }
}
