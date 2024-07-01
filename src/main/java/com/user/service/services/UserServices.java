/**
 * 
 */
package com.user.service.services;

import java.util.List;

import com.user.service.entities.User;

/**
 * 
 */
public interface UserServices {
	
	User saveUsers(User user);
	
	List<User> getAllUsers();
	
	User getUser(String userId);

}
