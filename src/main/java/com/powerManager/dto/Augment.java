package com.powerManager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Augment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAugument;

    @Column
    private String augmentText;

    @ManyToOne
    @JoinColumn(name="power", nullable=false)
    private Power power;


}
