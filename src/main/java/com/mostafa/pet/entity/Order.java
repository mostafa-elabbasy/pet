package com.mostafa.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "pet_order")
public class Order implements Serializable {

    private static final long serialVersionUID = -777849687031348L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price" )
    private Double price;

    @NotNull
    @Column(name = "currency" , nullable = false)
    private String currency;

    @JsonIgnoreProperties(value = {"order"} , allowSetters = true)
    @OneToOne(cascade = { CascadeType.ALL } , fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id" , referencedColumnName = "id")
    private Pet pet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", pet=" + pet +
                '}';
    }
}
