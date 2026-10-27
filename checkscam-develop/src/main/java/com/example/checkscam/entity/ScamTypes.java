package com.example.checkscam.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "scam_types")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScamTypes extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
}
