package com.mario.DiningReview.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="USER_TABLE")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(name="DISPLAY_NAME")
    @NonNull
    private String displayName;

    @Column(name="CITY")
    private String city;

    @Column(name="STATE")
    private String state;

    @Column(name="ZIPCODE")
    private String zipcode;

    @Column(name="PEANUT_ALLERGIES")
    private Boolean peanutAllergies;

    @Column(name="EGG_ALLERGIES")
    private Boolean eggAllergies;

    @Column(name="DAIRY_ALLERGIES")
    private Boolean dairyAllergies;
}
