/**
 * 
 */
package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.entities.User;
import com.user.service.exception.ResourceNotFoundException;
import com.user.service.external.HotelService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserServices;

/**
 * 
 */
@Service
public class UserServiceServiceImpl implements UserServices{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private HotelService hotelService;

	@Override
	public User saveUsers(User user) {
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepo.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	@Override
	public User getUser(String userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found !! UsedId : "+userId));
		//http://localhost:8081/v1/users/user?userId=12023983-9cbe-4c3f-8a5a-cfbc9c5603bf
		Rating[] ratingDetails = restTemplate.getForObject("http://RATINGSERVICE/v1/rating/getAllRatings/user/"+user.getUserId(), Rating[].class);
		List<Rating> ratingList = Arrays.stream(ratingDetails).toList();
		List<Rating> collect = ratingList.stream().map(rating ->{
			//api call to hotel service
			// http://localhost:8082/v1/hotel/getHotel/028824a6-2f56-4808-891e-2189e9aa0801
			
			//ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/v1/hotel/getHotel/"+rating.getHotelId(), Hotel.class);
			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());
		user.setUserRating(collect);
		return user;
	}

}
