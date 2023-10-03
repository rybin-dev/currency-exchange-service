package com.rybindev.currencyexchangeservice.mapper;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.rybindev.currencyexchangeservice.model.ExchangeResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeResponseJsonConverter implements Mapper<ExchangeResponse, JsonObject>{
    private static final ExchangeResponseJsonConverter INSTANCE = new ExchangeResponseJsonConverter();
    private static final JsonKey BASE_CURRENCY = Jsoner.mintJsonKey("baseCurrency", null);
    private static final JsonKey TARGET_CURRENCY = Jsoner.mintJsonKey("targetCurrency", null);
    private static final JsonKey RATE = Jsoner.mintJsonKey("rate", null);
    private static final JsonKey AMOUNT = Jsoner.mintJsonKey("amount", null);
    private static final JsonKey CONVERT_AMOUNT = Jsoner.mintJsonKey("convertedAmount", null);

    private final CurrencyDtoJsonConverter currencyDtoJsonConverter = CurrencyDtoJsonConverter.getInstance();

    public static ExchangeResponseJsonConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public JsonObject mapFrom(ExchangeResponse object) {
        JsonObject base = currencyDtoJsonConverter.mapFrom(object.getBaseCurrency());
        JsonObject target = currencyDtoJsonConverter.mapFrom(object.getTargetCurrency());

        JsonObject jsonObject = new JsonObject();
        jsonObject.put(BASE_CURRENCY,base);
        jsonObject.put(TARGET_CURRENCY,target);
        jsonObject.put(RATE,object.getRate());
        jsonObject.put(AMOUNT,object.getAmount());
        jsonObject.put(CONVERT_AMOUNT,object.getConvertedAmount());

        return jsonObject;
    }
}
