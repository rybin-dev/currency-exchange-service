package com.rybindev.currencyexchangeservice.servlet;

import com.github.cliftonlabs.json_simple.JsonKey;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/error")
public class ErrorHandlerServlet extends HttpServlet {
    private static final JsonKey MESSAGE_KEY = Jsoner.mintJsonKey("message", null);


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req,resp);
    }

    public void handleRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        // String servletName = (String) request.getAttribute("jakarta.servlet.error.servlet_name");
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");

        JsonObject jsonObject = new JsonObject();
        if (throwable == null && statusCode == null) {
            jsonObject.put(MESSAGE_KEY, "Error Information Is Missing");
        } else {
            jsonObject.put(MESSAGE_KEY, throwable.getClass().getName());
        }

        PrintWriter writer = resp.getWriter();
        writer.write(jsonObject.toJson());
        writer.close();
    }
}
