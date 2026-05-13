package org.cat.irere.repository;

import org.cat.irere.model.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuantityRepository extends JpaRepository<Quantity, Long> {

    List<Quantity> findByProductCode(String productCode);

    @Query("SELECT SUM(CASE WHEN q.operation = 'IN' THEN q.quantity ELSE -q.quantity END) " +
            "FROM Quantity q WHERE q.productCode = :productCode")
    Integer getAvailableQuantity(@Param("productCode") String productCode);
}