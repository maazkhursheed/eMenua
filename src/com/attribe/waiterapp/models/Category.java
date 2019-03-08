package com.attribe.waiterapp.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sabih Ahmed on 5/11/2015.
 */
public class Category {

    private int id;
    private String name;
    private String created_at;
    private String updated_at;
    private String image;
    private byte[] imageBlob;
    private transient boolean selected;
    private transient Drawable mCarouselDrawable;
    


    /**
     *
     * @param id
     * @param name
     * @param imageUrl
     */
    public Category(int id, String name, String imageUrl, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.image = imageUrl;
        this.created_at = created_at;
        this.updated_at = updated_at;
        
    }

    public Category(int id, String name, byte[] imageBlob ,String created_at, String updated_at){
        this.id = id;
        this.name = name;
        this.imageBlob = imageBlob;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


	public String getImageUrl() {
        return image;
    }

    public void setImageUrl(String imageUrl) {
        this.image = image;
    }


    public byte[] getImageBlob() {
        return imageBlob;
    }

    public void setImageBlob(byte[] imageBlob) {
        this.imageBlob = imageBlob;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setCarouselImage(Drawable drawable){

        this.mCarouselDrawable = drawable;
    }

    public Drawable getCarouselImage() {
        return mCarouselDrawable;
    }
}
