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

import webshop.entities.Color;
import webshop.services.ColorService;

@RestController 
@CrossOrigin(origins = {"http://localhost:3000"})
public class ColorController {
	
	private final ColorService colorService;
	
	public ColorController(ColorService colorService) {
		
		this.colorService = colorService;
	}
	
	@GetMapping("/colors")
	public List<Color> getAllSizes(){
		
		return colorService.getAllColors();
	}
	
	@PostMapping("/colors")
	public Color addColor(@RequestBody Color color) {
		
		return colorService.addColor(color);
		
	}
	@PutMapping("/colors/{colorId}")
    public Color updateColor(@PathVariable("colorId") Long colorId,
                                                 @RequestBody Color updatedColor) {
        return colorService.updateColor(colorId, updatedColor);
    }
	@DeleteMapping("/colors/{colorid}")  
	public void deleteColor(@PathVariable("colorid") Long colorid)   
	{  
		colorService.delete(colorid);  
	} 
	
	

}
