package com.example.savoryrestaurant.data;

import java.util.ArrayList;
import java.util.List;
import com.example.savoryrestaurant.R;
import com.example.savoryrestaurant.model.MenuItem;

public class MenuRepository {

    private static MenuRepository instance;
    private final List<MenuItem> menuItems = new ArrayList<>();

    private MenuRepository() {
        // TEMP sample data (remove later)
        menuItems.add(new MenuItem(
                "Herb-Crusted Lamb Chops",
                "£18.50",
                "CHEF'S PICK",
                R.drawable.lamb
        ));
    }

    public static MenuRepository getInstance() {
        if (instance == null) {
            instance = new MenuRepository();
        }
        return instance;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }
}
