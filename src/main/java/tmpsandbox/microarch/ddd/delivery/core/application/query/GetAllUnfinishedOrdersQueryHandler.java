package tmpsandbox.microarch.ddd.delivery.core.application.query;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;


/*
Получить все незавершенные заказы (статус Created и Assigned)
Мы можем отслеживать на карте заказы. Ответ данного Use Case должен содержать информацию о заказе и его местоположении.
Поля Query:
- В данном Query нет полей
Response:
- Список DTO или error. Поля DTO:
    - ID заказа
    - Location заказа
 */
@Service
public class GetAllUnfinishedOrdersQueryHandler {
    @PersistenceContext
    private EntityManager em;

    public List<GetAllUnfinishedOrdersQueryResponse> handle() {
        String sql = """
            SELECT
                id,
                x, 
                y
            FROM orders
            """;

        return (List<GetAllUnfinishedOrdersQueryResponse>) em.createNativeQuery(sql, GetAllUnfinishedOrdersQueryResponse.class)
            .getResultList();
    }
}
