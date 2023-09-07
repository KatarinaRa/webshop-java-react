package webshop.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import webshop.entities.Size;
import webshop.services.SizeService;

@RestController 
@CrossOrigin(origins = {"http://localhost:3000"})
public class SizeController {
	
	private final SizeService sizeService;
	
	public SizeController(SizeService sizeService) {
		
		this.sizeService = sizeService;
		
	}
	
	@GetMapping("/sizes")
	public List<Size> getAllSizes(){
		
		return sizeService.getAllSizes();
	}
	
	@PostMapping("/sizes")
	public Size addSize(@RequestBody Size size) {
		return sizeService.addSize(size);
		
	}
	@PutMapping("/sizes/{sizeId}")
    public Size updateSize(@PathVariable("sizeId") Long sizeId,
                                                 @RequestBody Size updatedSize) {
        return sizeService.updateSize(sizeId, updatedSize);
    }

    @DeleteMapping("/sizes/{sizeId}")
    public void deleteProductCategory(@PathVariable("sizeId") Long sizeId) {
        sizeService.delete(sizeId);
    }
    
    @GetMapping("/name/{sizeId}")
    public String getSizeName(@PathVariable("sizeId") Long sizeId) {
    	Optional<Size> size= sizeService.findById(sizeId);
    	return size.get().getName();
    	
    	
    }
	
	
	
	

}
