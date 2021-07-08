package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name="restaurant")

@NamedQueries({
        @NamedQuery(name="getAllRestaurants",query = "select a from RestaurantEntity a"),
        @NamedQuery(name="getAllRestaurantsByName",
                query = "select a from RestaurantEntity a " +
                        "where   lower(a.restaurantName) like lower(concat('%', :restaurantName,'%')) " +
                        "ORDER BY a.restaurantName ASC"),
        @NamedQuery(name="getRestaurantByUuid",
                query = "select a from RestaurantEntity a " +
                        "where   a.uuid = :restaurantUuid " ),

})
public class RestaurantEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="uuid")
    private String uuid;

    @Column(name="restaurant_name")
    private String restaurantName;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(name="customer_rating")
    private BigDecimal customerRating;

    @Column(name="average_price_for_two")
    private Integer averagePrice;

    @Column(name="number_of_customers_rated")
    private Integer noOfCustomerRated;

    @OneToOne
    @JoinColumn(name="address_id")
    private AddressEntity addressEntity;

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public BigDecimal getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(BigDecimal customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Integer averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getNoOfCustomerRated() {
        return noOfCustomerRated;
    }

    public void setNoOfCustomerRated(Integer noOfCustomerRated) {
        this.noOfCustomerRated = noOfCustomerRated;
    }

    public AddressEntity getAddressEntity() {
        return addressEntity;
    }

    public void setAddressEntity(AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(restaurantName, that.restaurantName) && Objects.equals(photoUrl, that.photoUrl) && Objects.equals(customerRating, that.customerRating) && Objects.equals(averagePrice, that.averagePrice) && Objects.equals(noOfCustomerRated, that.noOfCustomerRated) && Objects.equals(addressEntity, that.addressEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, restaurantName, photoUrl, customerRating, averagePrice, noOfCustomerRated, addressEntity);
    }

    @Override
    public String toString() {
        return "RestaurantEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", customerRating=" + customerRating +
                ", averagePrice=" + averagePrice +
                ", noOfCustomerRated=" + noOfCustomerRated +
                ", addressEntity=" + addressEntity +
                '}';
    }
}
