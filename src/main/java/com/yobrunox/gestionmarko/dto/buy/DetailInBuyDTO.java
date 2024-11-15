package com.yobrunox.gestionmarko.dto.buy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class DetailInBuyDTO {
    List<DetailBuyGetDTO> detailsBuy;
    private UUID idType;
}
