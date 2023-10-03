package com.rybindev.currencyexchangeservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Currency {
    private Integer id;
    private String code;
    private String name;
    private String sign;
}
