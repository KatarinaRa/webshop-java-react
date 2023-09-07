package webshop.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String imageURL;
	
	private String description;
	
	private Double price;
	
	
	@JsonBackReference
	 @OneToMany(mappedBy = "product")
	 private List<ProductSize> productSizes;
	 
	public List<ProductSize> getProductSizes() {
		return productSizes;
	}

	public void setProductSizes(List<ProductSize> productSizes) {
		this.productSizes = productSizes;
	}

	@ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;
	
	@ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;
    
    
    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory category;
    
    private String Drzava;

	public String getDrzava() {
		return Drzava;
	}

	public void setDrzava(String drzava) {
		Drzava = drzava;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	
    
    
}