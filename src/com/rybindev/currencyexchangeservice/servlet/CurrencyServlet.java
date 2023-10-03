package com.rybindev.currencyexchangeservice.servlet;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.rybindev.currencyexchangeservice.CurrencyRunner;
import com.rybindev.currencyexchangeservice.exception.BadRequestException;
import com.rybindev.currencyexchangeservice.mapper.CurrencyDtoJsonConverter;
import com.rybindev.currencyexchangeservice.model.CurrencyDto;
import com.rybindev.currencyexchangeservice.service.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final CurrencyDtoJsonConverter currencyDtoJsonConverter = CurrencyDtoJsonConverter.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            CurrencyRunner.main(new String[1]);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyCode = req.getRequestURI().replace("/currencies", "").replace("/", "");
        PrintWriter writer = resp.getWriter();
        if (currencyCode.isEmpty()) {
            writer.write(currencyDtoJsonConverter.mapFrom(currencyService.findAll()).toJson());
        } else {
            writer.write(currencyDtoJsonConverter.mapFrom(currencyService.findByCode(currencyCode)).toJson());
        }

        writer.close();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name == null || code == null || sign == null) {
            throw new BadRequestException();
        }

        CurrencyDto createCurrency = CurrencyDto.builder()
                .name(name)
                .code(code)
                .sign(sign)
                .build();

        CurrencyDto save = currencyService.save(createCurrency);
        JsonObject jsonObject = currencyDtoJsonConverter.mapFrom(save);

        PrintWriter writer = resp.getWriter();
        writer.write(jsonObject.toJson());
        writer.close();

    }
}
