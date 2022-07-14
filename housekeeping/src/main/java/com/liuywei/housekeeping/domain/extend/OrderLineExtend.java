package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.OrderLine;
import com.liuywei.housekeeping.domain.Product;

public class OrderLineExtend extends OrderLine {

    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
