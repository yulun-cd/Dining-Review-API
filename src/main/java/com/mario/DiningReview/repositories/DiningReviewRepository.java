package com.mario.DiningReview.repositories;

import com.mario.DiningReview.models.DiningReview;
import com.mario.DiningReview.models.ReviewStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {
    List<DiningReview> findByReviewStatus(ReviewStatus reviewStatus);
    List<DiningReview> findByReviewStatusAndRestaurantId(ReviewStatus reviewStatus, Long restaurantId);
}
