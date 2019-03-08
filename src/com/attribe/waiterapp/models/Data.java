package com.attribe.waiterapp.models;

import java.util.ArrayList;

/**
 * Created by Sabih Ahmed on new_order-Jun-15.
 */
public class Data {

    private int id;
    private String name;
    private String created_at;
    private String updated_at;
    private String image;
    private byte[] imageBlob;
    private ArrayList <Item> menus = new ArrayList<Item>();

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Item> getMenus() {
        return menus;
    }

    public void setMenus(ArrayList<Item> menus) {
        this.menus = menus;
    }

    public class Menu{
        private Integer id;
        private String name;
        private Integer price;
        private String description;
        private Integer categoryId;
        private String createdAt;
        private String updatedAt;
        private ArrayList<Images> images = new ArrayList<Images>();

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public ArrayList<Images> getImages() {
            return images;
        }

        public void setImages(ArrayList<Images> images) {
            this.images = images;
        }


        public class Images{


        private Integer id;
        private String url;
        private String created_at;
        private String updated_at;


            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
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
        }
    }
}
