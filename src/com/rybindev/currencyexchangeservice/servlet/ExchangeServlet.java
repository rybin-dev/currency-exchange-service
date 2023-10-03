package com.rybindev.currencyexchangeservice.servlet;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.rybindev.currencyexchangeservice.exception.BadRequestException;
import com.rybindev.currencyexchangeservice.mapper.ExchangeResponseJsonConverter;
import com.rybindev.currencyexchangeservice.model.ExchangeRequest;
import com.rybindev.currencyexchangeservice.model.ExchangeResponse;
import com.rybindev.currencyexchangeservice.service.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ExchangeResponseJsonConverter exchangeResponseJsonConverter = ExchangeResponseJsonConverter.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (from == null || to == null || amount == null || !amount.matches("[0-9]+(\\.[0-9]+)?")){
            throw new  BadRequestException();
        }

        ExchangeRequest exchangeRequest = new ExchangeRequest(from,
                to,
                new BigDecimal(amount));

        ExchangeResponse exchangeResponse = exchangeService.exchange(exchangeRequest);
        JsonObject jsonObject = exchangeResponseJsonConverter.mapFrom(exchangeResponse);

        PrintWriter writer = resp.getWriter();
        writer.write(jsonObject.toJson());
        writer.close();
    }
}
