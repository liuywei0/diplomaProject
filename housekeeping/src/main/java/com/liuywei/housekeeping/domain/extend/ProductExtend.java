package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Category;
import com.liuywei.housekeeping.domain.Product;

public class ProductExtend extends Product {

    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
