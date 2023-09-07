package webshop.services;

import org.springframework.stereotype.Service;

import webshop.entities.User;
import webshop.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	public User getUserById(Long id) {
		return userRepository.findUserById(id);
	};

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

   
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
}
