package webshop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import webshop.entities.Size;
import webshop.repositories.SizeRepository;

@Service
public class SizeService {
    private final SizeRepository sizeRepository;

    public SizeService(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    public Size addSize(Size size) {
        return sizeRepository.save(size);
    }

    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }
    
    public Size updateSize(Long sizeId, Size updatedSize) {
		Size existingSize = sizeRepository.findById(sizeId)
				.orElseThrow(() -> new IllegalArgumentException("Ne postoji size  s ID-om: " + sizeId));

		existingSize.setName(updatedSize.getName());
	

		return sizeRepository.save(existingSize);
	}
    public Optional<Size> findById(Long id) {
    	return sizeRepository.findById(id);
    }
    
	public void delete(Long sizeId) {
		sizeRepository.deleteById(sizeId);
	}
}