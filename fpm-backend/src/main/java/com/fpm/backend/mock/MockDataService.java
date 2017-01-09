package com.fpm.backend.mock;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fpm.backend.BluemixService;
import com.fpm.backend.DataService;
import com.fpm.backend.data.*;

/**
 * Mock data model. This implementation has very simplistic locking and does not
 * notify users of modifications.
 */
public class MockDataService extends DataService {


	private static MockDataService INSTANCE;

    private List<SystemX> systemXs;
    private List<Category> categories;
    private int nextSystemXId = 0;
    
    private Client client;
    private WebTarget target;

    @PostConstruct
    protected void init() {
        client = ClientBuilder.newClient();
        //example query params: ?q=Turku&cnt=10&mode=json&units=metric
        target = client.target(BluemixService.url)
     	       .queryParam("accesskey", BluemixService.accessKey)
    	       .queryParam("username", BluemixService.username)
    	       .queryParam("password", BluemixService.password);
                ;
    }

    private MockDataService() {
    	this.init();
        categories = MockDataGenerator.createCategories();
        systemXs = MockDataGenerator.createSystemXs(categories);
        nextSystemXId = systemXs.size() + 1;
    }

    public synchronized static DataService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MockDataService();
        }
        return INSTANCE;
    }

    @Override
    public synchronized List<SystemX> getAllSystemXs() {
        return systemXs;
    }

    @Override
    public synchronized List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public synchronized void updateSystemX(SystemX p) {
        if (p.getId() < 0) {
            // New systemX
            p.setId(nextSystemXId++);
            systemXs.add(p);
            return;
        }
        for (int i = 0; i < systemXs.size(); i++) {
            if (systemXs.get(i).getId() == p.getId()) {
            	p = getAnomalyDetection(p);
                systemXs.set(i, p);
                return;
            }
        }

        throw new IllegalArgumentException("No systemX with id " + p.getId()
                + " found");
    }

    @Override
    public synchronized SystemX getSystemXById(int systemXId) {
        for (int i = 0; i < systemXs.size(); i++) {
            if (systemXs.get(i).getId() == systemXId) {
                return systemXs.get(i);
            }
        }
        return null;
    }

    @Override
    public synchronized void deleteSystemX(int systemXId) {
        SystemX p = getSystemXById(systemXId);
        if (p == null) {
            throw new IllegalArgumentException("SystemX with id " + systemXId
                    + " not found");
        }
        systemXs.remove(p);
    }

	@Override
	public SystemX getAnomalyDetection(SystemX systemX) {
		
		systemX.setAnomalousField("Field1");
		systemX.setAnomalyIndex(1.3);
		
		systemX = BluemixService.callAnomalyService(systemX, target);
		return systemX;
	}
}
