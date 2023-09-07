package webshop.services;

import java.util.List;

import org.springframework.stereotype.Service;

import webshop.entities.Color;
import webshop.repositories.ColorRepository;


@Service
public class ColorService {
	private final ColorRepository colorRepository;
	
	
	public ColorService(ColorRepository colorRepository) {
		
		this.colorRepository = colorRepository;
	}
	
	public Color addColor(Color color) {
		
		return colorRepository.save(color);
		
	}
	public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
	 public Color updateColor(Long colorId, Color updatedColor) {
			Color existingColor = colorRepository.findById(colorId)
					.orElseThrow(() -> new IllegalArgumentException("Ne postoji size  s ID-om: " + colorId));

			existingColor.setName(updatedColor.getName());
			return colorRepository.save(existingColor);
		}

	public void delete(Long id)   
	{  
		colorRepository.deleteById(id);  
	}  


}
