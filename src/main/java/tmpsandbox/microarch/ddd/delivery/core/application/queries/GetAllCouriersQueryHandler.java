package tmpsandbox.microarch.ddd.delivery.core.application.queries;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

/*
Получить всех курьеров
Мы можем отслеживать на карте курьеров, которые выполняют заказ. Ответ данного Use Case должен содержать информацию о курьере и его местоположение.

Поля Query:
- В данном Query нет полей

Response:
Список DTO или error. Поля DTO:
- ID курьера
- Name курьера
- Location курьера
 */
@Service
public class GetAllCouriersQueryHandler {
    @PersistenceContext
    private EntityManager em;

    public List<GetAllCouriersQueryResponse> handle() {
        String sql = """
            SELECT
                id,
                name,
                CONCAT(x, ',', y) location
            FROM couriers
            """;

        return (List<GetAllCouriersQueryResponse>) em.createNativeQuery(sql, GetAllCouriersQueryResponse.class)
            .getResultList();
    }
}
