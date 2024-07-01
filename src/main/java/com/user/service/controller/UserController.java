/**
 * 
 */
package com.user.service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.entities.User;
import com.user.service.services.UserServices;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

/**
 * 
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	@Autowired
	private UserServices userService;
	
	@PostMapping("/addUser")
	public ResponseEntity<User> createUser(@RequestBody User user){
		
		User newUser = userService.saveUsers(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
		
	}
	
	@GetMapping("/user")
	//@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
	@Retry(name = "ratingHotelRetry", fallbackMethod = "ratingHotelFallback")
	//@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getUser(@RequestParam String userId){
		User user = userService.getUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){
		logger.info("CircuitBreaker Executed for {} ",ex.getMessage());
		ex.getStackTrace();
		User user = User.builder().userEmail("dummy@email.com")
				.userId("123456")
				.userName("Dummy User")
				.UserAddress("World")
				.build();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/allUsers")
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUsers = userService.getAllUsers();
		return ResponseEntity.ok(allUsers);
	}
}
