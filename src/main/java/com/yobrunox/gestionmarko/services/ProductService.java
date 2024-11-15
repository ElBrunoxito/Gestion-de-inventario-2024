package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.categoryAndUnit.CategoryAddDTO;
import com.yobrunox.gestionmarko.dto.product.ProductAddDTO;
import com.yobrunox.gestionmarko.dto.product.ProductGetAdminDTO;
import com.yobrunox.gestionmarko.dto.product.ProductGetUserDTO;
import com.yobrunox.gestionmarko.dto.product.ProductoGetDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.*;
import com.yobrunox.gestionmarko.repository.*;
import com.yobrunox.gestionmarko.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    //private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;
    private final UnitRepository unitRepository;
    private final CategoryService categoryService;
    private final UnitService unitService;

    //private fin

    /*@Transactional
    public String*/

    public List<ProductoGetDTO> getAll(Long id_Business){
        /*String username = jwtProvider.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Usuario no existente")

        );
        if(!user.getRoles().stream().anyMatch(role -> role.getRole() == ERole.ADMIN)){
            new BusinessException("M-403", HttpStatus.FORBIDDEN,"No tienes permiso para agregar productos");
        }*/

        //List<ProductoGetDTO> products = productRepository.getAllProducts(user.getId())

        return productRepository.getAllProducts(id_Business).orElse(new ArrayList<>());
        //return productRepository.getAllProducts(user.getId()).orElse(new ArrayList<>());
    }

    public ProductGetAdminDTO getProductByAdmin(UUID idProduct){
        return productRepository.getProductByIdAdmin(idProduct).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Ha ocurrido un error")
        );
       /* return productRepository.getProductById(idProduct).orElseThrow(
        );*/
    }
    public ProductGetUserDTO getProductByUser(UUID idProduct){
        return productRepository.getProductByIdUser(idProduct).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Ha ocurrido un error")
        );
    }


    @Transactional
    public Product createProduct(ProductAddDTO productAddDTO, UserEntity user){
        Business business = user.getBusiness();

        /*
        Unit unit = unitRepository.findById(productAddDTO.getIdUnit()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Unidades no encontradas")
        );*/
        Unit unit = unitService.getUnit(productAddDTO.getUnitAddFast());

        //String description = productAddDTO.getDescription();//.concat(" x "+unit.getName());

        CategoryAddDTO categoryFast = productAddDTO.getCategoryAddFast();
        categoryFast.setIdBusiness(user.getBusiness().getId());
        Category category = categoryService.getCategoryForBusiness(categoryFast);






        Product product = Product.builder()
                .barCode(productAddDTO.getBarCode())
                .description(productAddDTO.getDescription())
                .initialStock(productAddDTO.getInitialStock())
                .currentStock(productAddDTO.getInitialStock())
                .state(true)
                .category(category)
                .business(business)
                .unit(unit)
                .build();


        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(ProductAddDTO productAddDTO,UserEntity user){

        if(!productRepository.existsById(productAddDTO.getIdProduct())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado jeje");
        }

        ///////////////
        Business business = user.getBusiness();

        /*
        Unit unit = unitRepository.findById(productAddDTO.getIdUnit()).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Unidades no encontradas")
        );*/
        Unit unit = unitService.getUnit(productAddDTO.getUnitAddFast());

        //String description = productAddDTO.getDescription().concat(" x "+unit.getName());

        CategoryAddDTO categoryFast = productAddDTO.getCategoryAddFast();

        categoryFast.setIdBusiness(user.getBusiness().getId());



        Category category = categoryService.getCategoryForBusiness(categoryFast);

        Product product = productRepository.findById(productAddDTO.getIdProduct()).orElse(null);

        Long currentStock = product.getCurrentStock() - product.getInitialStock() + productAddDTO.getInitialStock();

        Product productForSave = Product.builder()
                .idProduct(productAddDTO.getIdProduct())
                .barCode(productAddDTO.getBarCode())
                .description(productAddDTO.getDescription())
                .initialStock(productAddDTO.getInitialStock())
                .currentStock(currentStock)
                .state(true)
                .category(category)
                .business(business)
                .unit(unit)
                .build();


        return productRepository.save(productForSave);

    }



    public void updateStock(Integer sumStock, UUID idProduct){
        Product product = productRepository.findById(idProduct).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado jeje")
        );

        Long currentStock = product.getCurrentStock();
        product.setCurrentStock(currentStock + sumStock);
        productRepository.save(product);

    }

    public void updateBuyUStock(Integer stock, Integer newStock, UUID idProduct){
        Product product = productRepository.findById(idProduct).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado jeje")
        );

        Long currentStock = product.getCurrentStock() - stock + newStock;
        product.setCurrentStock(currentStock);
        productRepository.save(product);
    }

    public void updateSaleUStock(Integer stock, Integer newStock, UUID idProduct){
        Product product = productRepository.findById(idProduct).orElseThrow(
                ()-> new BusinessException("M-400", HttpStatus.NOT_FOUND,"Producto no encontrado jeje")
        );

        Long currentStock = product.getCurrentStock() + stock - newStock;
        product.setCurrentStock(currentStock);
        productRepository.save(product);
    }




}
