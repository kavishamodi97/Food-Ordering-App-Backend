package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name="category")
@NamedQueries({
        @NamedQuery(
                name = "categoryByUuid",
                query = "select c from CategoryEntity c where c.uuid=:uuid order by c.categoryName"),
        @NamedQuery(
                name = "getAllCategoriesOrderedByName",
                query = "select c from CategoryEntity c order by c.categoryName asc"),
        @NamedQuery(
                name = "getCategoriesByRestaurant",
                query =
                        "Select c from CategoryEntity c where c.id in (select rc.category_id from RestaurantCategoryEntity rc where rc.restaurant_id = "
                                + "(select r.id from RestaurantEntity r where "
                                + " r.uuid=:restaurantUuid) )  order by c.categoryName")
})
public class CategoryEntity  implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name="category_name")
    private String categoryName;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<ItemEntity> items;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(categoryName, that.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, categoryName);
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
