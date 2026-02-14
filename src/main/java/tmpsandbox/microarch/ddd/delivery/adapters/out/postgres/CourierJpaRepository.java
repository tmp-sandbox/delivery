package tmpsandbox.microarch.ddd.delivery.adapters.out.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.Courier;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourierJpaRepository extends JpaRepository<Courier, UUID> {

    @Query("""
        SELECT c
        FROM Courier c
        WHERE EXISTS (
            SELECT 1
            FROM StoragePlace sp
            WHERE sp.courier = c
        )
        AND NOT EXISTS (
            SELECT 1
            FROM StoragePlace sp
            WHERE sp.courier = c AND sp.status <> tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.CourierStatus.EMPTY
        )
        """)
    List<Courier> findFree();

    @Query("""
        SELECT c
        FROM Courier c
        JOIN FETCH c.storagePlaces sp
        WHERE EXISTS (
            SELECT 1
            FROM StoragePlace sp
            WHERE sp.courier = c
        )
        AND EXISTS (
            SELECT 1
            FROM StoragePlace sp
            WHERE sp.courier = c AND sp.status = tmpsandbox.microarch.ddd.delivery.core.domain.model.courier.CourierStatus.BUSY
        )
        """)
    List<Courier> findBusy();
}
