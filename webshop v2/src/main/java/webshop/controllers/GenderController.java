package webshop.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webshop.entities.Gender;
import webshop.services.GenderService;


@RestController 
@CrossOrigin(origins = {"http://localhost:3000"})
public class GenderController {

	private final GenderService genderService;
	
	public GenderController (GenderService genderService) {
		
		this.genderService = genderService;
	}
	
	@GetMapping("/genders")
	public List<Gender> getAllGenders(){
		
		return genderService.getAllGenders();
	}
	
	@PostMapping("/genders")
	public Gender addGender(@RequestBody Gender gender) {
		return genderService.addGender(gender);
	}
	@PutMapping("/genders/{genderId}")
    public Gender updateGender(@PathVariable("genderId") Long genderId,
                                                 @RequestBody Gender updatedGender) {
        return genderService.updateGender(genderId, updatedGender);
    }
	@DeleteMapping("/genders/{genderid}")  
	public void deleteGender(@PathVariable("genderid") Long genderid)   
	{  
		genderService.delete(genderid);  
	} 
	
	
}
