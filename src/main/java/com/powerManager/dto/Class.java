package com.powerManager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class Class {

    @Id
    @Column(name = "id_class")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClass;

    @Column
    private String className;

    @OneToMany(mappedBy = "level",  cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClassLevel> classLevels;
}