package com.fpm.backend.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.fpm.backend.data.Ata;
import com.fpm.backend.data.Category;
import com.fpm.backend.data.SystemX;



public class MockDataGenerator {
    private static int nextCategoryId = 1;
    private static int nextSystemXId = 1;
    private static final Random random = new Random(1);
    private static final String categoryNames[] = new String[] {
            "Brakes System 1 FAULT", "Brakes System 2 FAULT", "ENG 1 HP VALVE FLT", "ENG 2 HP VALVE FLT" };

    private static String[] word1 = new String[] { "Please fix the BSCU1","Please fix the BSCU2", "The valve 1 need to be replaced", "The valve 2 is broken, need a fix asap" };

    private static String[] word2 = new String[] { "gardening",
            "living a healthy life", "designing tree houses", "home security",
            "intergalaxy travel", "meditation", "ice hockey",
            "children's education", "computer programming", "Vaadin TreeTable",
            "winter bathing", "playing the cello", "dummies", "rubber bands",
            "feeling down", "debugging", "running barefoot",
            "speaking to a big audience", "creating software", "giant needles",
            "elephants", "keeping your wife happy" };

    static List<Category> createCategories() {
        List<Category> categories = new ArrayList<Category>();
        for (String name : categoryNames) {
            Category c = createCategory(name);
            categories.add(c);
        }
        return categories;

    }

    static List<SystemX> createSystemXs(List<Category> categories) {
        List<SystemX> systemXs = new ArrayList<SystemX>();
        for (int i = 0; i < 100; i++) {
            SystemX p = createSystemX(categories);
            systemXs.add(p);
        }

        return systemXs;
    }

    private static Category createCategory(String name) {
        Category c = new Category();
        c.setId(nextCategoryId++);
        c.setName(name);
        return c;
    }

    private static SystemX createSystemX(List<Category> categories) {
        SystemX p = new SystemX();
        p.setId(nextSystemXId++);
        p.setSystemXName(generateName());

        p.setP1(new Double((random.nextInt(250) + 50) / 10.0));
        p.setP2(new Double((random.nextInt(250) + 50) / 10.0));
        p.setP3(new Double((random.nextInt(250) + 50) / 10.0));
        
        p.setAta(Ata.values()[random.nextInt(Ata
                .values().length)]);
        if (p.getAta() == Ata.PNEUMATICS) {
            p.setP1(random.nextInt(523));
        }

        p.setCategory(getCategory(categories, 1, 2));
        return p;
    }

    private static Set<Category> getCategory(List<Category> categories,
            int min, int max) {
        int nr = random.nextInt(max) + min;
        HashSet<Category> systemXCategories = new HashSet<Category>();
        for (int i = 0; i < nr; i++) {
            systemXCategories.add(categories.get(random.nextInt(categories
                    .size())));
        }

        return systemXCategories;
    }

    private static String generateName() {
        return word1[random.nextInt(word1.length)] ;
    }
}
