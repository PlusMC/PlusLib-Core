package org.plusmc.pluslib.bukkit.managed;

import org.plusmc.pluslib.bukkit.gui.GUIElement;

import java.util.HashMap;
import java.util.Map;

public abstract class PaginatedGUI extends PlusGUI {
    private int page = 0;
    private final Map<Integer, Map<Integer, GUIElement>> pages;


    protected PaginatedGUI() {
        super();
        this.pages = new HashMap<>();
        createPages();
        draw();
    }

    protected abstract void createPages();

    public int getPageAmount() {
        return this.pages.size();
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(final int page) {
        this.page = page;
        setElements(pages.getOrDefault(page, new HashMap<>()));
    }

    public void setPage(final int page, final boolean update) {
        this.page = page;
        setElements(pages.getOrDefault(page, new HashMap<>()));
        if (update)
            this.draw();
    }

    protected Map<Integer, Map<Integer, GUIElement>> getPages() {
        return this.pages;
    }

    protected void addPage(final int page) {
        this.pages.put(page, new HashMap<>());
        setElements(pages.getOrDefault(page, new HashMap<>()));
    }

    protected void addPage(final int page, final Map<Integer, GUIElement> elements) {
        this.pages.put(page, elements);
        setElements(pages.getOrDefault(page, new HashMap<>()));
    }

    protected Map<Integer, GUIElement> getPage(final int page) {
        return this.pages.get(page);
    }

    protected void removePage(final int page) {
        this.pages.remove(page);
        setElements(pages.getOrDefault(page, new HashMap<>()));
    }

    protected void removePage(final int page, final boolean update) {
        this.pages.remove(page);
        if (update)
            this.draw();
    }
}



