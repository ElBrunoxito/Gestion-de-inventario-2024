package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.models.DetailBuy;
import com.yobrunox.gestionmarko.models.DetailSale;
import com.yobrunox.gestionmarko.repository.DetailBuyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class MarginSaleBuyService {

/*
    private final DetailBuyRepository detailBuyRepository;

    @Transactional
    public DetailSale processSale(DetailSale sale) {
        List<DetailBuy> availableBuys = detailBuyRepository.findAvailableBuysByProductId(
                sale.getProduct().getIdProduct()
        );

        Double remainingToSell = sale.getQuantity();

        for (DetailBuy buy : availableBuys) {
            if (remainingToSell <= 0) break;

            Double usedQuantity = Math.min(remainingToSell, buy.getRemainingQuantity());
            buy.setRemainingQuantity(buy.getRemainingQuantity() - usedQuantity);
            remainingToSell -= usedQuantity;

            // Crear relación entre venta y compra
            SaleBuyRelation relation = new SaleBuyRelation();
            relation.setSale(sale);
            relation.setBuy(buy);
            relation.setQuantity(usedQuantity);

            sale.getSaleBuyRelations().add(relation);
        }

        if (remainingToSell > 0) {
            throw new IllegalStateException("No hay suficiente inventario para esta venta.");
        }

        detailBuyRepository.saveAll(availableBuys);
        return detailSaleRepository.save(sale);
    }*/
}
