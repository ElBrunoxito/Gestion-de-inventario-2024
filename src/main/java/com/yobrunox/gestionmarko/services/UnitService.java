package com.yobrunox.gestionmarko.services;


import com.yobrunox.gestionmarko.dto.categoryAndUnit.UnitAddDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.Unit;
import com.yobrunox.gestionmarko.repository.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UnitService {


    private final UnitRepository unitRepository;

    public Unit getUnit(UnitAddDTO unitFast) {

        System.out.println("aqui 1");

        //Long idBusiness = categoryFast.getIdBusiness();
        if ((unitFast.getId() == null || unitFast.getId() == 0) && (unitFast.getName() == null || unitFast.getName().isEmpty())) {
            throw new BusinessException("M-400", HttpStatus.BAD_REQUEST, "Campos vacios de la unidad");
        }
        System.out.println("aqui 2");

        unitFast.setName(unitFast.getName().toUpperCase());
        System.out.println("aqui 3 →" + unitFast.getName());


        //FIND IF CATEGORY IS CREATED OR CREATE CATEGORY IF CATEGORY ISN'T IN THE DATABASE

        if (unitRepository.existsByName(unitFast.getName())) {
            System.out.println("aqui 4");

            return unitRepository.findByName(unitFast.getName()).orElseThrow(
                    () -> new BusinessException("M-400", HttpStatus.BAD_REQUEST, "La unidad con name no exite")
            );
        } else if(unitFast.getName() != null) {
            return this.createUnit(unitFast);
        }else {
            throw new BusinessException("M-500", HttpStatus.BAD_REQUEST, "No se pudo crear la unidad por datos incorrectos, vuelva intentarlo");
        }

    }






    public Set<String> getUnits(){
        return unitRepository.getAllUnits().orElseThrow(
                ()-> new BusinessException("M-500", HttpStatus.NOT_FOUND,"Ha ocurrido un error al obtener unidades")
        );

    }

    public Unit createUnit(UnitAddDTO unitAddDTO){

        Unit unit = Unit.builder()
                .name(unitAddDTO.getName())
                .build();
        return unitRepository.save(unit);
    }
}
