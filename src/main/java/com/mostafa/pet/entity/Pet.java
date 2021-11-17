package com.mostafa.pet.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pet")
public class Pet implements Serializable {

    private static final long serialVersionUID = -777845527031348L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 50, nullable = false , name = "breed")
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "available")
    private Boolean available;

    @JsonIgnoreProperties(value = {"pet"} , allowSetters = true)
    @OneToOne(mappedBy = "pet" , cascade = { CascadeType.ALL } , fetch = FetchType.LAZY)
    private Order Order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Order getOrder() {
        return Order;
    }

    public void setOrder(Order order) {
        Order = order;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", available=" + available +
                ", Order=" + Order +
                '}';
    }
}
