package com.example.methods_of_jpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByProductName(String productName);

    List<Product> findAllByProductPriceBetween(double price1, double price2);

    List<Product> findAllByProductPriceGreaterThan(double price, Sort sort);

    Optional<Product> findByProductPriceAndProductBrand(double productPrice, String productBrand);

    void deleteByProductPrice(double productPrice);

    int countByProductPriceGreaterThanEqual(double productPrice);

    boolean existsByProductNameAndProductPrice(String productName, Double productPrice);

    Optional<Product> findByProductNameAndProductPrice(String productName, Double productPrice);






    //JPQL -> Positional parameters, named parameters

    @Query("SELECT p from Product p WHERE p.productPrice=?1 AND p.productBrand=?2") //positional parameters
    Optional<Product> getProduct(double price, String brandName);

    @Query("SELECT p from Product p WHERE p.productPrice=:price AND p.productBrand=: brandName") //named parameters
    Optional<Product> getProduct1(double price, String brandName);

    @Query("SELECT p from Product p WHERE p.productPrice=:price AND p.productBrand=: brandName") //named parameters
    Optional<Product> getProduct2(@Param("price")double price1, @Param("brandName")String brandName1);




    //Plain SQL or Raw SQL or Native SQL

    @Query(nativeQuery = true, value = "SELECT * FROM product where product_price=? AND product_brand=?")
    Optional<Product> getProduct3(double price, String name);

    @Query(nativeQuery = true, value = "SELECT * FROM product where product_price:=price AND product_brand:=name")
    Optional<Product> getProduct4(String name, double price);


    // @Modifying
    // @Transactional
    // @Query(nativeQuery = true, value = "UPDATE product SET product_price=? WHERE product_brand=?")
    // int updatePrice(double price, String brand);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Product p SET p.productPrice=:price WHERE p.productBrand=:brand") //value is optional in case of 1 attribute & compulsary in case of > 1
    int updatePrice(double price, String brand);



}
