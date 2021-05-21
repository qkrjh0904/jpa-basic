package com.jpa.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    @Column(name = "item_name")
    private String name;
    private int price;
    @Column(name = "stock_quantity")
    private int stockQuantity;



}
