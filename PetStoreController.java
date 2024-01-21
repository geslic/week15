package pet.store.controller.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody
			PetStoreData petStoreData) {
		log.info("Received a PUT request to /pet_store/{} with data: {}", petStoreId, 
		petStoreData);
		
		// Set the pet store ID in the pet store data from  the ID parameter
		petStoreData.setPetStoreId(petStoreId);
		
		// Call the savePetStore method in the service class
		PetStoreData updatedPetStoreData = petStoreService.savePetStore(petStoreData);
		
		return updatedPetStoreData;
	}
	
	
	@PostMapping
	public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Received a POST request to /pet_store with data: {}", petStoreData);
		
		// Call the service method to save or modify pet store data
		PetStoreData savedPetStoreData = petStoreService.savePetStore(petStoreData);
		
		// Return the saved or modified PetStoreData object
		return savedPetStoreData;
		}
	
	
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PetStoreEmployee> addEmployeeToPetStore(
			@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee employee) {
		log.info("Received a POST request to /pet_store/{}/employee with data: {}",
				petStoreId, employee);
		
		// Call the saveEmployee method in the pet store service and return the results
		PetStoreEmployee savedEmployee = petStoreService.saveEmployee(petStoreId, employee);
		
		// Return the saved employee along with HTTP status 201 (Created)
		return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
		}
	
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreCustomer addCustomer(
			@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomer customer) {
		log.info("Received a POST request to /pet_store/{}/employee with data: {}",
				petStoreId, customer);
		
		return petStoreService.saveCustomer(petStoreId, customer);
	}
	
	@GetMapping("/petstores")
	public List<PetStoreData> retrieveAllPetStores(){
		
		return petStoreService.getAllPetStores();
	}
	
	@GetMapping("/{petStoreId}")
	public PetStoreData retrievePetStore(@PathVariable Long petStoreId) {
		// Call the service method to retrieve a single pet store
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	
	@DeleteMapping("/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId)
	{
		// Log the request
		System.out.println("Deleting pet store with ID " + petStoreId);
		
		// Call the service method to delete the pet store
		petStoreService.deletePetStoreById(petStoreId);
		
		// Return a deletion successful message
		Map<String, String> response = new HashMap<>();
		response.put("message", "Pet store with ID " + petStoreId + " successfully deleted.");
		return response;
	
	}
}
