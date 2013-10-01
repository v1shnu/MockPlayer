package com.vishnu.mockplayer.models;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 20/9/13
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Action {
    private float x1, y1, x2, y2;
    private boolean menuButton, backButton;
    private int destination;

    public Action(float x1, float y1, float x2, float y2, boolean menuButton, boolean backButton, int destination) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.menuButton = menuButton;
        this.backButton = backButton;
        this.destination = destination;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public boolean isMenuButton() {
        return menuButton;
    }

    public void setMenuButton(boolean menuButton) {
        this.menuButton = menuButton;
    }

    public boolean isBackButton() {
        return backButton;
    }

    public void setBackButton(boolean backButton) {
        this.backButton = backButton;
    }
}
