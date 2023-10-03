package com.rybindev.currencyexchangeservice.mapper;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.rybindev.currencyexchangeservice.model.CurrencyDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyDtoJsonConverter implements Mapper<CurrencyDto, JsonObject> {
    private static final CurrencyDtoJsonConverter INSTANCE = new CurrencyDtoJsonConverter();
    private static final JsonKey ID = Jsoner.mintJsonKey("id", null);
    private static final JsonKey CODE = Jsoner.mintJsonKey("code", null);
    private static final JsonKey NAME = Jsoner.mintJsonKey("name", null);
    private static final JsonKey SING = Jsoner.mintJsonKey("sign", null);

    public static CurrencyDtoJsonConverter getInstance() {
        return INSTANCE;
    }


    public JsonArray mapFrom(List<CurrencyDto> currencyDtos) {
        return currencyDtos.stream()
                .map(this::mapFrom)
                .collect(Collectors.toCollection(JsonArray::new));


    }

    @Override
    public JsonObject mapFrom(CurrencyDto object) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put(ID, object.getId());
        jsonObject.put(CODE, object.getCode());
        jsonObject.put(NAME, object.getName());
        jsonObject.put(SING, object.getSign());
        return jsonObject;
    }

}
