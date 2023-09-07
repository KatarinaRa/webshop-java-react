package webshop.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @Enumerated(EnumType.STRING)
	 private ERole name;
	
	public Role() {

	  }

	  public Role(ERole name) {
	    this.name = name;
	  }


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ERole getName() {
	    return name;
	  }

	  public void setName(ERole name) {
	    this.name = name;
	  }

	
	 
	 


}
