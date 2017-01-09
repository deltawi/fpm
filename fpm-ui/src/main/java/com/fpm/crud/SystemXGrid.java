package com.fpm.crud;

import java.util.Collection;
import java.util.Locale;

import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;

import com.fpm.backend.data.Ata;
import com.fpm.backend.data.SystemX;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToDoubleConverter;
import com.vaadin.data.util.converter.StringToEnumConverter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * Grid of systemXs, handling the visual presentation and filtering of a set of
 * items. This version uses an in-memory data source that is suitable for small
 * data sets.
 */
public class SystemXGrid extends Grid {

    private StringToEnumConverter ataConverter = new StringToEnumConverter() {
        @Override
        public String convertToPresentation(Enum ata,
                java.lang.Class<? extends String> targetType, Locale locale)
                throws Converter.ConversionException {
            String text = super.convertToPresentation(ata, targetType,
                    locale);

            String color = "";
            if (ata == Ata.AIRCONDITIONING) {
                color = "#2dd085";
            } else if (ata == Ata.FUEL) {
                color = "#ffc66e";
            } else if (ata == Ata.PNEUMATICS) {
                color = "#f54993";
            }

            String iconCode = "<span class=\"v-icon\" style=\"font-family: "
                    + FontAwesome.CIRCLE.getFontFamily() + ";color:" + color
                    + "\">&#x"
                    + Integer.toHexString(FontAwesome.CIRCLE.getCodepoint())
                    + ";</span>";

            return iconCode + " " + text;
        };
    };

    public SystemXGrid() {
        setSizeFull();

        setSelectionMode(SelectionMode.SINGLE);

        BeanItemContainer<SystemX> container = new BeanItemContainer<SystemX>(
                SystemX.class);
        setContainerDataSource(container);
        System.out.println(this.getColumns().toString());
        setColumnOrder("id", "systemXName", "p1", "p2", "p3","anomalyIndex", "anomalousField", "ata", "category");

        // Show empty stock as "-"
        getColumn("p1").setConverter(new StringToDoubleConverter() {
            @Override
            public String convertToPresentation(Double value,
                    java.lang.Class<? extends String> targetType, Locale locale)
                    throws Converter.ConversionException {
                if (value == 0) {
                    return "-";
                }

                return super.convertToPresentation(value, targetType, locale);
            };
        });
        
        getColumn("p2").setConverter(new StringToDoubleConverter() {
            @Override
            public String convertToPresentation(Double value,
                    java.lang.Class<? extends String> targetType, Locale locale)
                    throws Converter.ConversionException {
                if (value == 0) {
                    return "-";
                }

                return super.convertToPresentation(value, targetType, locale);
            };
        });
        
        getColumn("p3").setConverter(new StringToDoubleConverter() {
            @Override
            public String convertToPresentation(Double value,
                    java.lang.Class<? extends String> targetType, Locale locale)
                    throws Converter.ConversionException {
                if (value == 0) {
                    return "-";
                }

                return super.convertToPresentation(value, targetType, locale);
            };
        });
        
        getColumn("anomalyIndex").setConverter(new StringToDoubleConverter() {
            @Override
            public String convertToPresentation(Double value,
                    java.lang.Class<? extends String> targetType, Locale locale)
                    throws Converter.ConversionException {
                if (value == 0) {
                    return "-";
                }

                return super.convertToPresentation(value, targetType, locale);
            };
        });

        // Add an traffic light icon in front of ata
        getColumn("ata").setConverter(ataConverter)
                .setRenderer(new HtmlRenderer());

        // Show categories as a comma separated list
        getColumn("category").setConverter(new CollectionToStringConverter());

        // Align columns using a style generator and theme rule until #15438
        setCellStyleGenerator(new CellStyleGenerator() {

            @Override
            public String getStyle(CellReference cellReference) {
                if (cellReference.getPropertyId().equals("p1")
                        || cellReference.getPropertyId().equals("p2") || cellReference.getPropertyId().equals("p3")) {
                    return "align-right";
                }
                return null;
            }
        });
    }

    /**
     * Filter the grid based on a search string that is searched for in the
     * systemX name, ata and category columns.
     *
     * @param filterString
     *            string to look for
     */
    public void setFilter(String filterString) {
        getContainer().removeAllContainerFilters();
        if (filterString.length() > 0) {
            SimpleStringFilter nameFilter = new SimpleStringFilter(
                    "systemXName", filterString, true, false);
            SimpleStringFilter ataFilter = new SimpleStringFilter(
                    "ata", filterString, true, false);
            SimpleStringFilter categoryFilter = new SimpleStringFilter(
                    "category", filterString, true, false);
            getContainer().addContainerFilter(
                    new Or(nameFilter, ataFilter, categoryFilter));
        }

    }

    private BeanItemContainer<SystemX> getContainer() {
        return (BeanItemContainer<SystemX>) super.getContainerDataSource();
    }

    @Override
    public SystemX getSelectedRow() throws IllegalStateException {
        return (SystemX) super.getSelectedRow();
    }

    public void setSystemXs(Collection<SystemX> systemXs) {
        getContainer().removeAllItems();
        getContainer().addAll(systemXs);
    }

    public void refresh(SystemX systemX) {
        // We avoid updating the whole table through the backend here so we can
        // get a partial update for the grid
        BeanItem<SystemX> item = getContainer().getItem(systemX);
        if (item != null) {
            // Updated systemX
            MethodProperty p = (MethodProperty) item.getItemProperty("id");
            p.fireValueChange();
        } else {
            // New systemX
            getContainer().addBean(systemX);
        }
    }

    public void remove(SystemX systemX) {
        getContainer().removeItem(systemX);
    }
}
