package com.webapp.finance.service;

import com.webapp.finance.model.Category;
import com.webapp.finance.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Long addCategory(Category category) {
        List<Category> categories = this.getAllCategories();
        Category cat =  categoryRepository.save(category);
        return cat.getId();
    }
}
