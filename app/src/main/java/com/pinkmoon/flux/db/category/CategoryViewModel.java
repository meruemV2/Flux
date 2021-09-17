package com.pinkmoon.flux.db.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * A ViewModel class for our Category object. Any database operations should
 * be called using this ViewModel, not the CategoryRepository nor the CategoryDao.
 */
public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;

    private LiveData<List<Category>> allCategoriesAlphabetically;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        categoryRepository = new CategoryRepository(application);

        allCategoriesAlphabetically = categoryRepository.getAllCategoriesAlphabetically();
    }

    public void insertCategory(Category category) {
        categoryRepository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }

    public void deleteCategory(Category category) {
        categoryRepository.deleteCategory(category);
    }

    public LiveData<List<Category>> getAllCategoriesAlphabetically() {
        return allCategoriesAlphabetically;
    }
}
