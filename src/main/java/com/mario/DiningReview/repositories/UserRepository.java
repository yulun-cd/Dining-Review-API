package com.mario.DiningReview.repositories;

import com.mario.DiningReview.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByDisplayName(String displayName);
    Boolean existsUserByDisplayName(String displayName);
}
