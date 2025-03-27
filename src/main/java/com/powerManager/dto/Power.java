package com.powerManager.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "power")
@Table(name = "power")
@Getter
@Setter
public class Power {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPower;
    @Column
    private String powerName;

    @Column
    private Integer powerCost;
    @Column
    private String powerDescription;
    @Column
    private boolean isAugmentable;
    @OneToMany(mappedBy="power")
    private List<PowerClass> powerClass;
    @OneToMany(mappedBy="power")
    private List<Augment> augments;
}
