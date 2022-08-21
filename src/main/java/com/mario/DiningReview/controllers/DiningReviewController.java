package com.mario.DiningReview.controllers;

import com.mario.DiningReview.models.*;
import com.mario.DiningReview.repositories.DiningReviewRepository;
import com.mario.DiningReview.repositories.RestaurantRepository;
import com.mario.DiningReview.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class DiningReviewController {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DiningReviewRepository diningReviewRepository;

    public DiningReviewController(UserRepository userRepository, RestaurantRepository restaurantRepository, DiningReviewRepository diningReviewRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.diningReviewRepository = diningReviewRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "test true";
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User newUser) {
        if(this.userRepository.existsUserByDisplayName(newUser.getDisplayName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Display name already exists!");
        } else {
            return this.userRepository.save(newUser);
        }
    }

    @PutMapping("/user/{displayName}")
    public User updateUser(@PathVariable String displayName, @RequestBody User newUser) {
        Optional<User> existUserOptional = this.userRepository.findByDisplayName(displayName);

        if(existUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        User existUser = existUserOptional.get();
        if(newUser.getCity() != null) {
            existUser.setCity(newUser.getCity());
        }
        if(newUser.getState() != null) {
            existUser.setState(newUser.getState());
        }
        if(newUser.getZipcode() != null) {
            existUser.setZipcode(newUser.getZipcode());
        }
        if(newUser.getPeanutAllergies() != null) {
            existUser.setPeanutAllergies(newUser.getPeanutAllergies());
        }
        if(newUser.getEggAllergies() != null) {
            existUser.setEggAllergies(newUser.getEggAllergies());
        }
        if(newUser.getDairyAllergies() != null) {
            existUser.setDairyAllergies(newUser.getDairyAllergies());
        }
        return this.userRepository.save(existUser);
    }

    @GetMapping("/user/{displayName}")
    public User getUser(@PathVariable String displayName) {
        Optional<User> existUserOptional = this.userRepository.findByDisplayName(displayName);

        if(existUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } else {
            return existUserOptional.get();
        }
    }

    @PostMapping("/review")
    public DiningReview createReview(@RequestBody DiningReview diningReview) {
        if(this.userRepository.existsUserByDisplayName(diningReview.getSubmitUser()) && this.restaurantRepository.existsById(diningReview.getRestaurantId())) {
            return this.diningReviewRepository.save(diningReview);
        } else if(this.restaurantRepository.existsById(diningReview.getRestaurantId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found, please register first or use a registered user name!");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant not found!");
        }
    }

    @GetMapping("/review/pending")
    public List<DiningReview> getPendingReviews() {
        return this.diningReviewRepository.findByReviewStatus(ReviewStatus.PENDING);
    }

    @PutMapping("/review/{id}")
    public DiningReview actOnReview(@PathVariable Long id, @RequestBody AdminReviewAction adminReviewAction) {
        Optional<DiningReview> reviewToUpdateOptional = this.diningReviewRepository.findById(id);
        if(reviewToUpdateOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found!");
        }

        DiningReview reviewToUpdate = reviewToUpdateOptional.get();

        if(reviewToUpdate.getReviewStatus() != ReviewStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review has already been approved or rejected.");
        } else {
            ReviewStatus newStatus = adminReviewAction.getAccept() ? ReviewStatus.ACCEPTED : ReviewStatus.REJECTED;
            reviewToUpdate.setReviewStatus(newStatus);

            List<DiningReview> reviews = this.diningReviewRepository.findByReviewStatusAndRestaurantId(ReviewStatus.ACCEPTED, reviewToUpdate.getRestaurantId());
            int[] counts = {0, 0, 0};
            int[] sums = {0, 0, 0};
            Restaurant restaurantToUpdate = this.restaurantRepository.findById(reviewToUpdate.getRestaurantId()).get();
            for(DiningReview review: reviews) {
                counts[0] += review.getPeanutScore() != 0 ? 1 : 0;
                sums[0] += review.getPeanutScore() != 0 ? review.getPeanutScore() : 0;
                counts[1] += review.getEggScore() != 0 ? 1 : 0;
                sums[1] += review.getEggScore() != 0 ? review.getEggScore() : 0;
                counts[2] += review.getDairyScore() != 0 ? 1 : 0;
                sums[2] += review.getDairyScore() != 0 ? review.getDairyScore() : 0;
            }
            restaurantToUpdate.setPeanutScore((double)sums[0]/(double)counts[0]);
            restaurantToUpdate.setEggScore((double)sums[1]/(double)counts[1]);
            restaurantToUpdate.setDairyScore((double)sums[2]/(double)counts[2]);
            this.restaurantRepository.save(restaurantToUpdate);

            return this.diningReviewRepository.save(reviewToUpdate);
        }
    }

    @PostMapping("/restaurant")
    public Restaurant addRestaurant(@RequestBody Restaurant newRestaurant) {
        if(this.restaurantRepository.existsRestaurantByZipcodeAndName(newRestaurant.getZipcode(), newRestaurant.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant already exists!");
        }
        return this.restaurantRepository.save(newRestaurant);
    }

    @GetMapping("restaurant/{id}")
    public Restaurant getRestaurant(@PathVariable Long id) {
        Optional<Restaurant> existRestaurantOptional = this.restaurantRepository.findById(id);

        if(existRestaurantOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found!");
        } else {
            return existRestaurantOptional.get();
        }
    }

    @GetMapping("restaurant/{zipcode}")
    public List<Restaurant> getNearbyFullyReviewedRestaurant(@PathVariable String zipcode) {
        return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(zipcode, 1.0, 1.0, 1.0);
    }

    @GetMapping("restaurant/recommendation/{displayName}")
    public List<Restaurant> giveRecommendation(@PathVariable String displayName) {
        Optional<User> theUserOptional = this.userRepository.findByDisplayName(displayName);
        if(theUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exists!");
        }
        User theUser = theUserOptional.get();
        if(theUser.getZipcode() == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Please update the user's information first!");
        } else if(theUser.getPeanutAllergies() && theUser.getEggAllergies() && theUser.getDairyAllergies()) {
            return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(theUser.getZipcode(), 4.0, 4.0, 4.0);
        } else if(theUser.getPeanutAllergies() && theUser.getEggAllergies()) {
            return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqual(theUser.getZipcode(), 4.0, 4.0);
        } else if(theUser.getPeanutAllergies() && theUser.getDairyAllergies()) {
            return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(theUser.getZipcode(), 4.0, 4.0);
        } else if(theUser.getEggAllergies() && theUser.getDairyAllergies()) {
            return this.restaurantRepository.findByZipcodeAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(theUser.getZipcode(), 4.0, 4.0);
        } else if(theUser.getPeanutAllergies()) {
            return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqual(theUser.getZipcode(), 4.0);
        } else if(theUser.getEggAllergies()) {
            return this.restaurantRepository.findByZipcodeAndEggScoreGreaterThanEqual(theUser.getZipcode(), 4.0);
        } else if(theUser.getDairyAllergies()) {
            return this.restaurantRepository.findByZipcodeAndDairyScoreGreaterThanEqual(theUser.getZipcode(), 4.0);
        } else {
            return this.restaurantRepository.findByZipcodeAndPeanutScoreGreaterThanEqualAndEggScoreGreaterThanEqualAndDairyScoreGreaterThanEqual(theUser.getZipcode(), 4.0, 4.0, 4.0);
        }
    }
}
