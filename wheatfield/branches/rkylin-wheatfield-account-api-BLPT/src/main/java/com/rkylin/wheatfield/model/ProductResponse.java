package com.rkylin.wheatfield.model;

import java.util.Map;

public class ProductResponse  extends CommonResponse{

    private Map<String, String> productMap;

    public Map<String, String> getProductMap() {
        return productMap;
    }

    public void setProductMap(Map<String, String> productMap) {
        this.productMap = productMap;
    }
    
    
}
