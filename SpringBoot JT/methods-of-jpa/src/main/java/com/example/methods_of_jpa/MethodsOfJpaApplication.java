package com.example.methods_of_jpa;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class MethodsOfJpaApplication implements CommandLineRunner {

	private final ProductRepository productRepository;
	public static void main(String[] args) {
		SpringApplication.run(MethodsOfJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Product product = Product.builder()	
							.productName("Iphone 16 pro max")
							.productBrand("Apple")
							.productPrice(150000.99)
							.build();


		//SAVE
		//productRepository.save(product);

		//SAVE ALL
		List<Product> products = getProducts();
		//productRepository.saveAll(products);

		//COUNT
		System.out.println("Total rows available in table: "+productRepository.count());


		//EXISTS
		//System.out.println("Is apple product exists: "+productRepository.existsById("275e0022-c0b4-4b17-9a67-23a247a56c02"));

		//DELETE
		//productRepository.deleteById("275e0022-c0b4-4b17-9a67-23a247a56c02");

		//FIND ALL
		List<Product> products2 = productRepository.findAll();
		//products2.forEach(System.out::println);

		List<Product> products3 = productRepository.findAll(Sort.by(Direction.ASC,"productPrice"));
		//products3.forEach(System.out::println);



		// PAGINATION
		
		// Page<Product> products4 = productRepository.findAll(PageRequest.of(0, 3));
		// Page<Product> products4 = productRepository.findAll(PageRequest.of(1, 3, Sort.by(Direction.DESC, "productPrice")));
		// products4.forEach(System.out::println);


		//FIND MY ID
		// Optional<Product> optProduct = productRepository.findById("44cb8dc0-bf2d-4c53-83f9-0381feb3478b");
		// Product existingProduct = optProduct.orElseThrow( () -> new NoSuchElementException("Product not found by id"));
		// System.out.println("Find by id: "+existingProduct);

		// SAVE or UPDATE -> if the id already exist in the table then update the obj otherwise insert the obj
		// Optional<Product> optProduct1 = productRepository.findById("44cb8dc0-bf2d-4c53-83f9-0381feb3478b");
		// Product existingProduct1 = optProduct1.orElseThrow( () -> new NoSuchElementException("Product not found by id"));
		
		// System.out.println("Before update: "+existingProduct1);

		// existingProduct1.setProductName("Iphone 17 pro max");
		// existingProduct1.setProductPrice(149000.99);
		
		// Product savedProduct = productRepository.save(existingProduct1);
		// System.out.println("Before update: "+existingProduct1);
		// System.out.println("After Update: "+savedProduct);




		//FIND BY PRODUCT NAME
		// Optional<Product> optProductByName = productRepository.findByProductName("Iphone 17 pro max");
		// Product productByName = optProductByName.orElseThrow(() -> new NoSuchElementException("Product does not exist"));
		// System.out.println("Product By Name: "+productByName);

		//FIND ALL BY PRODUCT PRICE BETWEEN

		// List<Product> productsBetweenRange = productRepository.findAllByProductPriceBetween(6000.00, 9000.00);
		// productsBetweenRange.forEach(System.out::println);


		//FIND ALL BY PRODUCT PRICE GREATER THAN

		// List<Product> productsGreaterThan = productRepository.findAllByProductPriceGreaterThan(6000.00, Sort.by(Direction.ASC, "productPrice"));
		// productsGreaterThan.forEach(System.out::println);

		//FIND BY PRODUCT PRICE & PRODUCT BRAND
		// Optional<Product> productByPriceAndBrand = productRepository.findByProductPriceAndProductBrand(149000.99,"Apple");
		// System.out.println("product by price and brand: "+productByPriceAndBrand.orElseThrow());

		//DELETE BY BRAND
		// productRepository.deleteByProductPrice(149000.99);

		//COUNT BY PRICE
		// int count = productRepository.countByProductPriceGreaterThanEqual(100000.00);
		// System.out.println("Products greater than price: "+count);

		//EXISTS BY NAME AND PRICE
		// String pname = "Iphone 17 pro max";
		// double pprice = 149000.99;
		// if(productRepository.existsByProductNameAndProductPrice(pname,pprice)){
		// 	Optional<Product> prod = productRepository.findByProductNameAndProductPrice(pname, pprice);
		// 	System.out.println("Product: "+prod.orElseThrow(() -> new NoSuchElementException("Product does not exists")));
		// }




















		//JPQL







		//GET PRODUCT

		// Optional<Product> productByPriceAndBrand = productRepository.getProduct(149000.99, "Apple");
		// System.out.println("Product by Price and Brand :"+productByPriceAndBrand);


		//GET PRODUCT 1

		// Optional<Product> productByPriceAndBrand1 = productRepository.getProduct1(149000.99, "Apple");
		// System.out.println("Product by Price and Brand :"+productByPriceAndBrand1);


		//GET PRODUCT 3
		// Optional<Product> productByPriceAndBrand2 = productRepository.getProduct3(149000.99, "Apple");
		// System.out.println("Product by Price and Brand :"+productByPriceAndBrand2);

		//GET PRODUCT 4
		// Optional<Product> productByPriceAndBrand3 = productRepository.getProduct4("Apple",149000.99);
		// System.out.println("Product by Price and Brand :"+productByPriceAndBrand3);


		transactionalConcept();


	}

	public void transactionalConcept(){
		//UPDATE PRICE
		int affectedRow = productRepository.updatePrice(180000.99, "Apple");
		System.out.println("No of rows affected "+affectedRow);

	}

	private List<Product> getProducts(){
		List<Integer> numbers = List.of(1,2,3,4,5,6,7,8,9);
		return numbers.stream()
						.map(number -> Product.builder()
								.productName("product - "+number)
								.productBrand("Brand - "+number)
								.productPrice(number * 1000.50)
								.build())
						.toList();
										
	}


}
