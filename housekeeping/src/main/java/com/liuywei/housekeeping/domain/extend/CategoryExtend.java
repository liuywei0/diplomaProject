package com.liuywei.housekeeping.domain.extend;

import com.liuywei.housekeeping.domain.Category;

import java.util.List;

public class CategoryExtend extends Category {

    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
