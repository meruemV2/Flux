package com.pinkmoon.flux.db.category;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pinkmoon.flux.db.FluxDB;

import java.util.List;

/**
 * Handles all operations that will interact with the DB.
 * All requests will be passed down to this class from its respective
 * ViewModel class. Do not call on methods from this class explicitly within
 * UI or business logic code. Use the ViewModel instead.
 */
public class CategoryRepository {

    private CategoryDao categoryDao;

    private LiveData<List<Category>> allCategoriesAlphabetically;

    public CategoryRepository(Application application) {
        FluxDB fluxDB = FluxDB.getInstance(application);

        categoryDao = fluxDB.categoryDao();

        allCategoriesAlphabetically = categoryDao.getAllCategoriesAlphabetically();
    }

    // Database operations
    public void insertCategory(Category category) {
        new InsertCategoryAsync(categoryDao).execute(category);
    }

    public void updateCategory(Category category) {
        new UpdateCategoryAsync(categoryDao).execute(category);
    }

    public void deleteCategory(Category category) {
        new DeleteCategory(categoryDao).execute(category);
    }

    public LiveData<List<Category>> getAllCategoriesAlphabetically() { return allCategoriesAlphabetically; }

    // AsyncTask operations
    public class InsertCategoryAsync extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        public InsertCategoryAsync(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.insertCategory(categories[0]);
            return null;
        }
    }

    public class UpdateCategoryAsync extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        public UpdateCategoryAsync(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.updateCategory(categories[0]);
            return null;
        }
    }

    public class DeleteCategory extends AsyncTask<Category, Void, Void> {
        private CategoryDao categoryDao;

        public DeleteCategory(CategoryDao categoryDao) {
            this.categoryDao = categoryDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoryDao.deleteCategory(categories[0]);
            return null;
        }
    }
}
