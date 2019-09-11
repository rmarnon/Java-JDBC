package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

				
		SellerDao sellerDao = DaoFactory.createSellerDao();//SellerDao eh interface!!!
		//Ao invez de new SellerDaoJDBC, Dessa forma o main nao conhece a implementacao, so a interface
		//Eh uma forma de fazer injecao de dependencia sem explicitar a implementacao
		
		System.out.println("=== Teste 1: Seller findById ===");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n=== Teste 2: Seller findByDepartment ===");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		list.forEach(System.out::println);
		}
		
}