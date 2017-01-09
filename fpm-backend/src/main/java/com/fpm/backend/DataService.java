package com.fpm.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.fpm.backend.data.Category;
import com.fpm.backend.data.SystemX;
import com.fpm.backend.mock.MockDataService;


public abstract class DataService implements Serializable{

    public abstract Collection<SystemX> getAllSystemXs();

    public abstract Collection<Category> getAllCategories();

    public abstract void updateSystemX(SystemX p);
    
    public abstract SystemX getAnomalyDetection(SystemX systemX);

    public abstract void deleteSystemX(int SystemId);

    public abstract SystemX getSystemXById(int SystemXId);

    public static DataService get() {
        return MockDataService.getInstance();
    }



}
