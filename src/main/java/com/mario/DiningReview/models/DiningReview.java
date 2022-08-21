package com.mario.DiningReview.models;

import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="DINING_REVIEW")
public class DiningReview {
    @Id
    @GeneratedValue
    private Long Id;

    @NonNull
    @Column(name="SUBMIT_USER")
    private String submitUser;

    @NonNull
    @Column(name="RESTAURANT_ID")
    private Long restaurantId;

    @Column(name="PEANUT_SCORE")
    private int peanutScore;

    @Column(name="EGG_SCORE")
    private int eggScore;

    @Column(name="DAIRY_SCORE")
    private int dairyScore;

    @Column(name="COMMENTARY")
    private String commentary;

    @Column(name="REVIEW_STATUS")
    private ReviewStatus reviewStatus = ReviewStatus.PENDING;
}
