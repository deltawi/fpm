package com.fpm.crud;

import com.example.fpm.MyUI;
import com.fpm.backend.DataService;
import com.fpm.backend.data.SystemX;

import java.io.Serializable;
import com.vaadin.server.Page;

/**
 * This class provides an interface for the logical operations between the CRUD
 * view, its parts like the systemX editor form and the data source, including
 * fetching and saving systemXs.
 *
 * Having this separate from the view makes it easier to test various parts of
 * the system separately, and to e.g. provide alternative views for the same
 * data.
 */
public class SampleCrudLogic implements Serializable {

    private SampleCrudView view;

    public SampleCrudLogic(SampleCrudView simpleCrudView) {
        view = simpleCrudView;
    }

    public void init() {
        editSystemX(null);
        // Hide and disable if not admin
        if (!MyUI.get().getAccessControl().isUserInRole("admin")) {
            view.setNewSystemXEnabled(false);
        }

        view.showSystemXs(DataService.get().getAllSystemXs());
    }

    public void cancelSystemX() {
        setFragmentParameter("");
        view.clearSelection();
        view.editSystemX(null);
    }

    /**
     * Update the fragment without causing navigator to change view
     */
    private void setFragmentParameter(String systemXId) {
        String fragmentParameter;
        if (systemXId == null || systemXId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = systemXId;
        }

        Page page = MyUI.get().getPage();
        page.setUriFragment("!" + SampleCrudView.VIEW_NAME + "/"
                + fragmentParameter, false);
    }

    public void enter(String systemXId) {
        if (systemXId != null && !systemXId.isEmpty()) {
            if (systemXId.equals("new")) {
                newSystemX();
            } else {
                // Ensure this is selected even if coming directly here from
                // login
                try {
                    int pid = Integer.parseInt(systemXId);
                    SystemX systemX = findSystemX(pid);
                    view.selectRow(systemX);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    private SystemX findSystemX(int systemXId) {
        return DataService.get().getSystemXById(systemXId);
    }

    public void saveSystemX(SystemX systemX) {
        view.showSaveNotification(systemX.getsystemXName() + " ("
                + systemX.getId() + ") updated");
        view.clearSelection();
        view.editSystemX(null);
        view.refreshSystemX(systemX);
        setFragmentParameter("");
    }

    public void deleteSystemX(SystemX systemX) {
        DataService.get().deleteSystemX(systemX.getId());
        view.showSaveNotification(systemX.getsystemXName() + " ("
                + systemX.getId() + ") removed");

        view.clearSelection();
        view.editSystemX(null);
        view.removeSystemX(systemX);
        setFragmentParameter("");
    }

    public void editSystemX(SystemX systemX) {
        if (systemX == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(systemX.getId() + "");
        }
        view.editSystemX(systemX);
    }

    public void newSystemX() {
        view.clearSelection();
        setFragmentParameter("new");
        view.editSystemX(new SystemX());
    }

    public void rowSelected(SystemX systemX) {
        if (MyUI.get().getAccessControl().isUserInRole("admin")) {
            view.editSystemX(systemX);
        }
    }
}
