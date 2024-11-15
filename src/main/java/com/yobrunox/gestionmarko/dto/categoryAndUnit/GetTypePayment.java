package com.yobrunox.gestionmarko.dto.categoryAndUnit;

import com.yobrunox.gestionmarko.models.Collect;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTypePayment {
    private Integer idTypePayment;
    private String name;
}
