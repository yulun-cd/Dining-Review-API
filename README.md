# Dining-Review-API

This API enables users to post dining reviews for restaurants, with specific allergic information. It also supports recommending nearby restaurants to a user
according to his/her location.

## Endpoints:
- `user` endpoint: allow creating new user and update existing user, as well as getting a full list of all users.
  - `GET` - `'/user'`: get a json output of a full list of all the existing users.
  - `POST` - `'/user'`: create a new user. This must have a request body as a json object. The request body must have a unique `displayName` for the user, and can
  have the follwing optional fields:
    - `city`, `state`, `zipcode`: the geographical location information of the user.
    - `peanutAllergies`, `eggAllergies`, `dairyAllergies`: boolean values incicating whether the user is allergy to a specific nutrient.
  - `GET` - `'/user/{displayName}'`: get the full information (json format) of the user with the `displayName`.
  - `PUT` - `'/user/{displayName}'`: update an existing user with the `displayName`. This must have a request body with the field(s) and value(s) being the 
  information of the user that you wish to modify.

- `restaurant` endpoint: allow creating and modifying restaurants. Also include recommending restaurants to users.
  - `POST` - `'/restaurant'`: create a new restaurant. This must have a request body. The request body must have a unique `name` and a `zipcode`.
  - `GET` - `'/restaurant/{id}'`: get the full information of the restaurant with the `id`. This is an attribute of restaurants that is auto-generated upon creation.
  - `GET` - `'/restaurant/nearby/{zipcode}'`: find all restaurants with the given `zipcode`. Only those restaurants with at least one review for each allergy category
  will be returned.
  - `GET` - `'/restaurant/recommendation/{displayName}'`: recommend restaurants for the user with the given valid `displayName`. The recommendation is based on 
  the geographical location and allergic information of the user. Only restaurants with average review score(s) higher than or equal to 3 in the user-activated 
  allergy category(s) will be recommended. For example, if user A is allergy to peanut and dairy, and is not allergy to egg, then only nearby restaurants with
  average scores of peanut and dairy allergy reviews higher than or equal to 3 will be recommended to user A.
  
- `review` endpoint: allow posting and acting on reviews.
  - `GET` - `'/review'`: get a full list of all reviews.
  - `GET` - `'/review/accepted'`, `'/review/pending'`, `'/review/rejected'`: get a full list of all the accepted/pending/rejected reviews.
  - `GET` - `'/review/restaurantId'`: get all reviews for the restaurant with `restaurantId`.
  - `POST` - `'/review'`: create a review. This must have a request body. The request body must have a `submitUser` which is the `displayName` of the reviewer,
  and it also must have a `restaurantId`, which is the `Id` of the restaurant to be reviewed. Optional fields of a review include:
    - `peanutScore`, `eggScore`, `dairyScore`: the review score for the peanut/egg/dairy allergy menu, ranging from 1 to 5, with 1 being the lowest and 5 the highest.
    - `commentary`: a commentary attached with the review.
    - All reviews created are with the status `PENDING`, which can later be modified by an admin to either `ACCEPTED` or `REJECTED`.
  - `PUT` - `'/review/{id}'`: perform an admin act on the review with the given valid `id` by changing its status. It will also automatically updates the average 
  score(s) of the corresponding restaurant of the review.
  This must have a request body with a field `accept` being a bollean value indicating whether to accept the review.
