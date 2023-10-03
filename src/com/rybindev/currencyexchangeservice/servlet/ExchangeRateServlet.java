package com.rybindev.currencyexchangeservice.servlet;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.rybindev.currencyexchangeservice.exception.BadRequestException;
import com.rybindev.currencyexchangeservice.mapper.ExchangeRateDtoJsonConverter;
import com.rybindev.currencyexchangeservice.model.CreateExchangeRateDto;
import com.rybindev.currencyexchangeservice.model.ExchangeRateDto;
import com.rybindev.currencyexchangeservice.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.stream.Collectors.*;

@WebServlet("/exchangeRates/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final String RATE_PATTERN = "[0-9]+(\\.[0-9]+)?";
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ExchangeRateDtoJsonConverter exchangeRateDtoJsonConverter = ExchangeRateDtoJsonConverter.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String currencyCodes = req.getRequestURI().replace("/exchangeRates", "").replace("/", "");

        PrintWriter writer = resp.getWriter();
        if (currencyCodes.isEmpty()) {
            writer.write(exchangeRateService.findAll()
                    .stream()
                    .map(exchangeRateDtoJsonConverter::mapFrom)
                    .collect(toCollection(JsonArray::new))
                    .toJson());
        } else {
            if (currencyCodes.length() != 6) {
                throw new BadRequestException();
            }

            String base = currencyCodes.substring(0, 3);
            String target = currencyCodes.substring(3, 6);

            writer.write(exchangeRateDtoJsonConverter.mapFrom(
                    exchangeRateService.findByBaseCodeAndTargetCode(base, target)).toJson());
        }
        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null || !rate.matches(RATE_PATTERN)) {
            throw new BadRequestException();
        }

        CreateExchangeRateDto createExchangeRateDto = CreateExchangeRateDto.builder()
                .baseCurrencyCode(baseCurrencyCode)
                .targetCurrencyCode(targetCurrencyCode)
                .rate(new BigDecimal(rate))
                .build();

        ExchangeRateDto save = exchangeRateService.save(createExchangeRateDto);
        JsonObject jsonObject = exchangeRateDtoJsonConverter.mapFrom(save);

        PrintWriter writer = resp.getWriter();
        writer.write(jsonObject.toJson());
        writer.close();

    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyCodes = req.getRequestURI().replace("/exchangeRates", "").replace("/", "");

        Map<String, String> parameters = new HashMap<>();
        try (BufferedReader reader = req.getReader()) {
            reader
                    .lines()
                    .flatMap(e -> Arrays.stream(e.split("&")))
                    .map(e -> e.split("="))
                    .filter(e -> e.length == 2)
                    .forEach(e -> parameters.put(e[0], e[1]));
        }

        String rate = parameters.get("rate");

        if (currencyCodes.length() != 6 || rate == null || !rate.matches(RATE_PATTERN)) {
            throw new BadRequestException();
        }

        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3, 6);

        CreateExchangeRateDto createExchangeRateDto = CreateExchangeRateDto.builder()
                .baseCurrencyCode(baseCurrencyCode)
                .targetCurrencyCode(targetCurrencyCode)
                .rate(new BigDecimal(rate))
                .build();

        ExchangeRateDto save = exchangeRateService.update(createExchangeRateDto);
        JsonObject jsonObject = exchangeRateDtoJsonConverter.mapFrom(save);

        PrintWriter writer = resp.getWriter();
        writer.write(jsonObject.toJson());
        writer.close();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();

        if ("PATCH".equals(method)) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }

    }


}
