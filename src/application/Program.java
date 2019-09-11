package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				
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
		
		System.out.println("\n=== Teste 3: Seller findAll ===");
		list = sellerDao.findall();
		list.forEach(System.out::println);
		
		System.out.println("\n=== Teste 4: Seller insert ===");
		Seller newSeller = new Seller(null, "Thiago Borges", "thiago@gmail", sdf.parse("22/11/1980"), 1450.00, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted: New Id = " + newSeller.getId());
		System.out.println(newSeller);
		
		System.out.println("\n=== Teste 5: Seller update ===");
		seller = sellerDao.findById(4);
		seller.setName("Layce Santos");
		seller.setEmail("lg@santos");
		seller.setBirthDate(sdf.parse("11/04/1990"));
		seller.setBaseSalary(1350.00);
		seller.setDepartment(department);
		sellerDao.update(seller);
		System.out.println("Seller Update " + seller);
		
		}
		
}