package webshop.services;

import java.util.List;

import org.springframework.stereotype.Service;

import webshop.entities.Gender;
import webshop.repositories.GenderRepository;

@Service
public class GenderService {
	
	private final GenderRepository genderRepository;
	
	public GenderService(GenderRepository genderRepository) {
		
		this.genderRepository = genderRepository;
	}
	
	public Gender addGender(Gender gender) {
		
		return genderRepository.save(gender);
	}
	
	public List<Gender> getAllGenders(){
		
		return genderRepository.findAll();
	}
	 public Gender updateGender(Long genderId, Gender updatedGender) {
			Gender existingGender = genderRepository.findById(genderId)
					.orElseThrow(() -> new IllegalArgumentException("Ne postoji gender  s ID-om: " + genderId));

			existingGender.setName(updatedGender.getName());
		

			return genderRepository.save(existingGender);
		}

	public void delete(Long id)   
	{  
		genderRepository.deleteById(id);  
	}
	


}
