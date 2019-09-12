package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		List<Department> list = new ArrayList<>();
		Department dep = new Department(null, "Spacial");
		
		System.out.println("===Teste1: Insert ===");				
		depDao.insert(dep);
		System.out.println("Inserted Department Id = " + dep.getId());
		System.out.println(dep);
		
		System.out.println("\n===Teste 2: deleteById===");
		depDao.deletById(5);
		System.out.println("Deleted Sucess! ");
		
		System.out.println("\n===Teste 3: findById===");
		dep = depDao.findById(2);
		System.out.println(dep);
		
		System.out.println("\n===Teste 4: findByAll===");		
		list.addAll(depDao.findAll());
		list.forEach(System.out::println);

		System.out.println("\n===Teste 5: update===");
		dep = depDao.findById(14);
		dep.setName("Bank");		
		depDao.update(dep);
		System.out.println("Department Update " + dep);
				
	}

}
