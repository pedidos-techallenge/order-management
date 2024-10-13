package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class MySQLOrderRepository implements IOrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MySQLOrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Value("${spring.custom.db-type}")
    private String dbType;  // Injeta o tipo de banco de dados

    public String getTableName(String tableName) {
        if ("mysql".equalsIgnoreCase(dbType)) {
            return "`" + tableName + "`"; // MySQL
        } else {
            return "\"" + tableName + "\""; // H2
        }
    }

    @Transactional
    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO dbtechchallange." + getTableName("order") + " (id, number_order, status) values (:id, :number_order, :status)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", order.getId());
        params.addValue("number_order", order.getNumberOrder());
        params.addValue("status", order.getStatus());
        namedParameterJdbcTemplate.update(sql, params);

        addItem(order.getItems());

        return order;
    }

    @Override
    public void update(Order order) {
        StringBuilder sql = new StringBuilder("UPDATE dbtechchallange." + getTableName("order") + " SET ");
        Map<String, Object> params = new HashMap<>();

        sql.append("number_order = :numberOrder, ");
        params.put("numberOrder", order.getNumberOrder());

        sql.append("status = :status, ");
        params.put("status", order.getStatus());

        sql.delete(sql.length() - 2, sql.length());

        sql.append(" WHERE id = :id");
        params.put("id", order.getId());

        namedParameterJdbcTemplate.update(sql.toString(), params);
    }


    @Transactional
    @Override
    public void addItem(List<Item> items){

        String sql = "INSERT INTO dbtechchallange." + getTableName("item") + "  (order_id, sku, quantity, unit_value) values (:order_id, :sku, :quantity, :unitValue)";

        SqlParameterSource[] batch = new SqlParameterSource[items.size()];
        for (int i = 0; i < items.size(); i++) {
            batch[i] = new BeanPropertySqlParameterSource(items.get(i));
        }

        namedParameterJdbcTemplate.batchUpdate(sql, batch);
    }


    @Override
    public Order getByOrderNumber(int order_number) {
        String sql = "SELECT * FROM dbtechchallange." + getTableName("order") + " WHERE number_order = :order_number";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("order_number", order_number);


        return namedParameterJdbcTemplate.queryForObject(sql, params, new RowMapper<Order>() {
            @Override
            public Order mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {

                return new Order(rs.getString("id"),
                        rs.getInt("number_order"),
                        rs.getString("status"));
            }
        });
    }

    @Override
    public List<Order> getOrders() {
        String sql = "SELECT * FROM dbtechchallange." + getTableName("order") + " \n" +
                     "WHERE status IN ('Recebido', 'Em Preparacao', 'Pronto')\n" +
                     "ORDER BY \n" +
                     "  CASE \n" +
                     "    WHEN status = 'Pronto' THEN 1\n" +
                     "    WHEN status = 'Em Preparacao' THEN 2\n" +
                     "    WHEN status = 'Recebido' THEN 3    \n" +
                     "  END,\n" +
                     "  number_order ASC; ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(sql, params, new OrderRowMapper());
    }

    @Override
    public int getLastNumber() {
        String sql = "SELECT MAX(number_order) FROM dbtechchallange." + getTableName("order");
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public int count() {
        String sql = "SELECT count(*) FROM dbtechchallange." + getTableName("order");
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Transactional
    @Override
    public Order get(String ordemId) {
        String sql = "SELECT * FROM dbtechchallange." + getTableName("order") + " WHERE id = :ordemId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ordemId", ordemId);

        List<Item> items = this.getItems(ordemId);

        return namedParameterJdbcTemplate.queryForObject(sql, params, new RowMapper<Order>() {
            @Override
            public Order mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {

                return new Order(rs.getString("id"),
                        rs.getInt("number_order"),
                        (List<Item>) items,
                        rs.getString("status"));
            }
        });
    }


    private List<Item> getItems(String ordemId){
        String sql = "SELECT * FROM dbtechchallange." + getTableName("order") + " WHERE order_id = :ordemId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ordemId", ordemId);
        List<Item> items = new ArrayList<>();

        namedParameterJdbcTemplate.query(sql, params, new RowCallbackHandler() {
            public void processRow(@NotNull ResultSet rs) throws SQLException {
                items.add(new Item(ordemId, rs.getString("sku"), rs.getInt("quantity"), rs.getFloat("unit_value")));
            }
        });
        return items;
    }

    private static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Order(
                    rs.getString("id"),
                    rs.getInt("number_order"),
                    rs.getString("status")
            );
        }
    }
}
