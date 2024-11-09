package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepositoryNoSQL;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DocumentDBOrderRepository implements IOrderRepositoryNoSQL {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Order get(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Order.class);
    }

    @Override
    public void create(Order order) {
        mongoTemplate.save(order);
    }

    @Override
    public void update(Order order) {
        mongoTemplate.save(order);
    }

    @Override
    public Order getByOrderNumber(int number) {
        Query query = new Query(Criteria.where("numberOrder").is(number));
        return mongoTemplate.findOne(query, Order.class);
    }

    @Override
    public List<Order> getOrders() {
        List<Order> orders = mongoTemplate.findAll(Order.class);

        return orders.stream()
                .filter(order ->
                        "Recebido".equals(order.getStatus()) ||
                                "EmPreparacao".equals(order.getStatus()) ||
                                "Pronto".equals(order.getStatus()))
                .sorted((o1, o2) -> {
                    int statusOrder1 = getStatusOrder(o1.getStatus());
                    int statusOrder2 = getStatusOrder(o2.getStatus());
                    int statusComparison = Integer.compare(statusOrder1, statusOrder2);
                    if (statusComparison != 0) {
                        return statusComparison;
                    }
                    return Integer.compare(o1.getNumberOrder(), o2.getNumberOrder());
                })
                .collect(Collectors.toList());
    }

    private int getStatusOrder(String status) {
        switch (status) {
            case "Pronto":
                return 1;
            case "EmPreparacao":
                return 2;
            case "Recebido":
                return 3;
            default:
                return 4;
        }
    }

    @Override
    public int getLastNumber() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "numberOrder"));
        query.limit(1);
        Order order = mongoTemplate.findOne(query, Order.class);
        return (order != null) ? order.getNumberOrder() : 0;
    }

    @Override
    public int count() {
        Query query = new Query();
        return (int) mongoTemplate.count(query, Order.class);
    }

    @Override
    public void clearDB() {
        mongoTemplate.remove(new Query(), "order");
    }
}
