package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;

@Data
@NoArgsConstructor
public class PetStoreCustomer {
	private Long customerID;
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;
	
	// Constructor that takes a Customer object
	public PetStoreCustomer(Customer customer) {
		customerID = customer.getCustomerID();
		customerFirstName = customer.getCustomerFirstName();
		customerLastName = customer.getCustomerLastName();
		customerEmail = customer.getCustomerEmail();
	}
	
	
}
