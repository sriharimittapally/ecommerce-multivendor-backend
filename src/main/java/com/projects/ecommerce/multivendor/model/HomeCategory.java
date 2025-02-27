package com.projects.ecommerce.multivendor.model;

import com.projects.ecommerce.multivendor.domain.HomeCategorySection;
import jakarta.persistence.*;



@Entity

public class HomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String image;
    private String categoryId;
    private HomeCategorySection section;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public HomeCategorySection getSection() {
        return section;
    }

    public void setSection(HomeCategorySection section) {
        this.section = section;
    }
}
