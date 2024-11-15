package com.yobrunox.gestionmarko.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeletingUUIDPdto {
    UUID id;
    Integer value;
    UUID idProduct;
}
