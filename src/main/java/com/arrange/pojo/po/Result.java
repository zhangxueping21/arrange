package com.arrange.pojo.po;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Result {
    private String[] unCheck;
    private String[] consent;
    private String[] refuse;
    private Map<String[],String> result = new HashMap<>();
}
