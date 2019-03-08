package com.attribe.waiterapp.models;

import java.io.Serializable;

/**
 * Created by Sabih Ahmed on 09-Jun-15.
 */
public class Image implements Serializable{

    private Integer id;
    private int menu_id;
    private String url;
    private String created_at;
    private String updated_at;
    private boolean selected;

    public Image(Integer id, String url, String created_at, String updated_at) {
        this.id = id;
        this.url = url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Image(Integer id, int menu_id,String url, String created_at, String updated_at) {
        this.id = id;
        this.menu_id = menu_id;
        this.url = url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public Integer getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }


    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public String getUpdated_at() {
        return updated_at;
    }


    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
