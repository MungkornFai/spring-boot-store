package com.mungkorn.springbootecommerceapi.payments;

import com.mungkorn.springbootecommerceapi.products.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Byte id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    public Category(String name) {
        this.name = name;
    }

    public Category(Byte id){
        this.id = id;
    }
}
