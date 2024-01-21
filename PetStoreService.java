package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	@Autowired
	private CustomerDao customerDao;
		
// methods
	
	@Transactional(readOnly = false)
	private PetStore findPetStoreById(Long petStoreId) {
		
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException(
						"Pet store with ID=" + petStoreId + " does not exist."));
	}
	
	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
	
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}
	
	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		
		return new PetStoreData(petStore);
	}
	
	@Transactional(readOnly = false)
	public PetStore findOrCreatePetStore(Long petStoreId) {
		
		PetStore petStore;
		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
		
	}
	@Transactional(readOnly = false)
	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		// Copy matching fields from PetStoreData to PetStore
		petStore.setPetStoreId(petStoreData.getPetStoreId());
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}
	
	public Employee findEmployeeById(Long petStoreId, Long employeeId) {
				
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException(
						"Employee with ID=" + employeeId + " does not exist."));
		
		// checking for pet store ID match
		if (employee.getPetStore().getPetStoreId() != petStoreId) {
			throw new IllegalArgumentException("Employee with ID=" + employeeId + 
					" is not employed at store with ID=" + petStoreId);
		}
		
		return employee;
	}
	
	
	public Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
	
		Employee employee;
		
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(petStoreId, employeeId);
		}
		
		return employee;
	}
	
	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		// Copy all matching PetStoreEmployee fields to the Employee object
		employee.setEmployeeID(petStoreEmployee.getEmployeeID());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
		
	}
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		
		PetStore petStore = findPetStoreById(petStoreId);
		
		Long employeeId = petStoreEmployee.getEmployeeID();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		
		// add employee to pet store
		petStore.getEmployees().add(employee);
		employee.setPetStore(petStore);	
		return new PetStoreEmployee(employeeDao.save(employee));
	}
	
	@Transactional(readOnly = true)
	public List<PetStoreData> getAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> petStoreDataList = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			
			//psd.getCustomers().clear();
			//psd.getEmployees().clear();
			
			petStoreDataList.add(psd);
		}
		
		return petStoreDataList;
	}
	
	@Transactional(readOnly = false)
	public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {

	PetStore petStore = findPetStoreById(petStoreId);
		
		Long customerId = petStoreCustomer.getCustomerID();
		Customer customer = findOrCreateCustomers(petStoreId, customerId);
		
		copyCustomerFields(customer, petStoreCustomer);
		customer.getPetStores().add(petStore);
		
		// add customer to pet store
		petStore.getCustomers().add(customer);
		
		return new PetStoreCustomer(customerDao.save(customer));
	}
	
	
	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		// TODO Auto-generated method stub
		// Copy all matching PetStoreEmployee fields to the Employee object
		customer.setCustomerID(petStoreCustomer.getCustomerID());		
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());		
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
	}
	private Customer findOrCreateCustomers(Long petStoreId, Long customerID) {
		Customer customer;
		if (Objects.isNull(customerID)) {
			// If the pet store ID is null, return a new Employee object
			customer = new Customer();
		}else {
			// If the pet store ID is not null, call the findEmployeeById method
			customer = findCustomerById(petStoreId, customerID);
		}
		return customer;
	}
	private Customer findCustomerById(Long petStoreId, Long customerID) {

		Customer customer = customerDao.findById(customerID)
				.orElseThrow(() -> new NoSuchElementException(
						"Customer with ID=" + customerID + " does not exist."));
	
		// Checking for pet store ID match with a pet store for customer should not throw an exception,
		// if the pet store ID exists, but is not a part of the customer data
		// it simply means that the customer should be added to the pet store via the join table.
		
		return customer;
		
		
		}
	
	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}
		
}
