package pet.store.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;

@Data
@NoArgsConstructor
public class PetStoreEmployee {
	private Long employeeID;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeePhone;
	private String employeeJobTitle;
	
	// Constructor that takes a Customer object
		public PetStoreEmployee(Employee employee) {
			this.employeeID = employee.getEmployeeID();
			this.employeeFirstName = employee.getEmployeeFirstName();
			this.employeeLastName = employee.getEmployeeLastName();
			this.employeePhone = employee.getEmployeePhone();
			this.employeeJobTitle = employee.getEmployeeJobTitle();
		}

}
