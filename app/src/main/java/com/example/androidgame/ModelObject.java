package com.example.androidgame;

public enum ModelObject {

    FIRST_PAGE(R.string.first_page, R.layout.first_page),
    SECOND_PAGE(R.string.second_page, R.layout.second_page),
    THIRD_PAGE(R.string.third_page, R.layout.third_page);

    private int id;
    private int layout;

    ModelObject(int id, int layout){
        this.id = id;
        this.layout = layout;
    }

    public int getLayout() {
        return layout;
    }

    public int getId() {
        return id;
    }
}
