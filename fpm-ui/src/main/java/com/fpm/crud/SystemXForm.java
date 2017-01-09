package com.fpm.crud;

import java.util.Collection;

import com.fpm.backend.DataService;
import com.fpm.backend.data.Ata;
import com.fpm.backend.data.Category;
import com.fpm.backend.data.SystemX;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

public class SystemXForm extends SystemXDesign {

	private SampleCrudLogic viewLogic;
    private BeanFieldGroup<SystemX> fieldGroup;

    public SystemXForm(SampleCrudLogic sampleCrudLogic) {
        super();
        //addStyleName("systemX-form");
        viewLogic = sampleCrudLogic;


        for (Ata s : Ata.values()) {
            ata.addItem(s);
        }

        fieldGroup = new BeanFieldGroup<SystemX>(SystemX.class);
        fieldGroup.bindMemberFields(this);

        // perform validation and enable/disable buttons while editing
        ValueChangeListener valueListener = new ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                formHasChanged();
            }
        };
        for (Field f : fieldGroup.getFields()) {
            f.addValueChangeListener(valueListener);
        }

        fieldGroup.addCommitHandler(new CommitHandler() {

            @Override
            public void preCommit(CommitEvent commitEvent)
                    throws CommitException {
            }

            @Override
            public void postCommit(CommitEvent commitEvent)
                    throws CommitException {
                DataService.get().updateSystemX(
                        fieldGroup.getItemDataSource().getBean());
            }
        });

        save.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    fieldGroup.commit();

                    // only if validation succeeds
                    SystemX systemX = fieldGroup.getItemDataSource().getBean();
                    viewLogic.saveSystemX(systemX);
                } catch (CommitException e) {
                    Notification n = new Notification(
                            "Please re-check the fields", Type.ERROR_MESSAGE);
                    n.setDelayMsec(500);
                    n.show(getUI().getPage());
                }
            }
        });

        cancel.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                viewLogic.cancelSystemX();
            }
        });

        delete.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                SystemX systemX = fieldGroup.getItemDataSource().getBean();
                viewLogic.deleteSystemX(systemX);
            }
        });
    }

    public void setCategories(Collection<Category> categories) {
        category.setOptions(categories);
    }

    public void editSystemX(SystemX systemX) {
        if (systemX == null) {
            systemX = new SystemX();
        }
        fieldGroup.setItemDataSource(new BeanItem<SystemX>(systemX));

        // before the user makes any changes, disable validation error indicator
        // of the systemX name field (which may be empty)
        systemXName.setValidationVisible(false);

        // Scroll to the top
        // As this is not a Panel, using JavaScript
        String scrollScript = "window.document.getElementById('" + getId()
                + "').scrollTop = 0;";
        Page.getCurrent().getJavaScript().execute(scrollScript);
    }

    private void formHasChanged() {
        // show validation errors after the user has changed something
        systemXName.setValidationVisible(true);

        // only systemXs that have been saved should be removable
        boolean canRemoveSystemX = false;
        BeanItem<SystemX> item = fieldGroup.getItemDataSource();
        if (item != null) {
            SystemX systemX = item.getBean();
            canRemoveSystemX = systemX.getId() != -1;
        }
        delete.setEnabled(canRemoveSystemX);
    }
}
