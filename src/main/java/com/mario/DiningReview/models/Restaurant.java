package com.mario.DiningReview.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="RESTAURANT")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long Id;

    @Column(name="NAME")
    @NonNull
    private String name;

    @Column(name="ZIPCODE")
    @NonNull
    private String zipcode;

    @Column(name="PEANUT_SCORE")
    private double peanutScore;

    @Column(name="EGG_SCORE")
    private double eggScore;

    @Column(name="DAIRY_SCORE")
    private double dairyScore;
}
