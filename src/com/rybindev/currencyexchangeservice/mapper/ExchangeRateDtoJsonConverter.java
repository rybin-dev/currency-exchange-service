package com.rybindev.currencyexchangeservice.mapper;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.rybindev.currencyexchangeservice.model.CreateExchangeRateDto;
import com.rybindev.currencyexchangeservice.model.ExchangeRateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateDtoJsonConverter implements Mapper<ExchangeRateDto, JsonObject> {
    private static final ExchangeRateDtoJsonConverter INSTANCE = new ExchangeRateDtoJsonConverter();
    private static final JsonKey ID = Jsoner.mintJsonKey("id", null);
    private static final JsonKey BASE = Jsoner.mintJsonKey("baseCurrency", null);
    private static final JsonKey TARGET = Jsoner.mintJsonKey("targetCurrency", null);
    private static final JsonKey RATE = Jsoner.mintJsonKey("rate", null);

    private final CurrencyDtoJsonConverter currencyDtoJsonConverter = CurrencyDtoJsonConverter.getInstance();

    public static ExchangeRateDtoJsonConverter getInstance() {
        return INSTANCE;
    }


    @Override
    public JsonObject mapFrom(ExchangeRateDto object) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ID, object.getId());
        jsonObject.put(BASE, currencyDtoJsonConverter.mapFrom(object.getBase()));
        jsonObject.put(TARGET, currencyDtoJsonConverter.mapFrom(object.getTarget()));
        jsonObject.put(RATE, object.getRate());
        return jsonObject;
    }
}
