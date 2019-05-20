package com.example.a1649618.model;

/**
 * Enumeration of note categories, represented as colors.
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public enum Category {

    RED(1), ORANGE(2), YELLOW(3), GREEN(4), LIGHT_BLUE(5), DARK_BLUE(6), PURPLE(7), BROWN(8);

    private int colorId;

    // create a category with a specific color ID.
    Category(int colorId) {
        this.colorId = colorId;
    }

    /**
     * Get the category's color ID.
     * @return
     */
    public int getColorId() {
        return colorId;
    }
}
