package webshop.DTO;

public class SizeAndQuantityDTO {
	
	private String size;
    private int quantity;


    public SizeAndQuantityDTO() {}

    public SizeAndQuantityDTO(String size, int quantity) {
        this.size = size;
        this.quantity = quantity;
    }

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
    
    

}
