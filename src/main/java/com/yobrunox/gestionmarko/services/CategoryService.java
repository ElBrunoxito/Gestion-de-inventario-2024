package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.categoryAndUnit.CategoryAddDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.Business;
import com.yobrunox.gestionmarko.models.Category;
import com.yobrunox.gestionmarko.repository.BusinessRepository;
import com.yobrunox.gestionmarko.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;


    //Aqui voy a devolver la categoria, si es creada o buscada por name todo en MAYUSCULA
    public Category getCategoryForBusiness(CategoryAddDTO categoryFast) {

        Long idBusiness = categoryFast.getIdBusiness();
        if ((categoryFast.getId() == null || categoryFast.getId() == 0) && (categoryFast.getName() == null || categoryFast.getName().isEmpty())) {
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST, "Campos vacios de la categoria");
        }
        if(categoryFast.getName()!=null) categoryFast.setName(categoryFast.getName().toUpperCase());

        //FIND IF CATEGORY IS CREATED OR CREATE CATEGORY IF CATEGORY ISN'T IN THE DATABASE

        if (categoryRepository.existsByIdAndBusiness_Id(categoryFast.getId(), idBusiness)) {
            return categoryRepository.findCategoryByIdAndBusiness_Id(categoryFast.getId(), idBusiness).orElse(null);
        }
        if (categoryRepository.existsByNameAndBusiness_Id(categoryFast.getName(), idBusiness)) {
            return categoryRepository.findCategoryByNameAndBusiness_Id(categoryFast.getName(), idBusiness).orElseThrow(
                    () -> new BusinessException("M-400", HttpStatus.BAD_REQUEST, "La categoria con name no exite")
            );
        } else if(categoryFast.getName() != null) {
            return this.createCategory(categoryFast);
        }else {
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST, "No se pudo crear la categoria por datos incorrectos, vuelva intentarlo");
        }

    }




    public Set<String> getCategories(Long idBusiness){
     return categoryRepository.getAllCategories(idBusiness).orElseThrow(
             ()-> new BusinessException("M-500", HttpStatus.NOT_FOUND,"Ha ocurrido un error")
     );

    }




    public Category createCategory(CategoryAddDTO categoryAddDTO){

        if(!businessRepository.existsById(categoryAddDTO.getIdBusiness())){
            throw new BusinessException("M-400", HttpStatus.NOT_FOUND,"No existe el negocio");
        }
        Business business = businessRepository.findById(categoryAddDTO.getIdBusiness()).orElse(null);
        Category category = Category.builder()
                .name(categoryAddDTO.getName())
                .business(business)
                .build();
        return categoryRepository.save(category);
    }

}


