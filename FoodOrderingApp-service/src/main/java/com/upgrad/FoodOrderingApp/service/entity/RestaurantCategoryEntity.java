package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="restaurant_category")

@NamedQueries({
        @NamedQuery(name="getRestaurantCategories",
                query = "select c from RestaurantCategoryEntity c " +
                        "where c.restaurantEntity.id = :restaurantId " +
                        "order by c.categoryEntity.categoryName ASC ")
})

public class RestaurantCategoryEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private RestaurantEntity restaurantEntity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity categoryEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RestaurantEntity getRestaurantEntity() {
        return restaurantEntity;
    }

    public void setRestaurantEntity(RestaurantEntity restaurantEntity) {
        this.restaurantEntity = restaurantEntity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantCategoryEntity that = (RestaurantCategoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(restaurantEntity, that.restaurantEntity) && Objects.equals(categoryEntity, that.categoryEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantEntity, categoryEntity);
    }

    @Override
    public String toString() {
        return "RestaurantCategoryEntity{" +
                "id=" + id +
                ", restaurantEntity=" + restaurantEntity +
                ", categoryEntity=" + categoryEntity +
                '}';
    }
}
