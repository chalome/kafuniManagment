/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.Color;

public class ModelNoticeBoard {

    public Color getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(Color titleColor) {
        this.titleColor = titleColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModelNoticeBoard(Color titleColor, String title, String time, String description) {
        this.titleColor = titleColor;
        this.title = title;
        this.time = time;
        this.description = description;
    }

    public ModelNoticeBoard() {
    }

    private Color titleColor;
    private String title;
    private String time;
    private String description;
}
