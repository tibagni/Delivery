package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.Order;
import com.delivery.util.LongPollingUtils;
import com.delivery.util.SQLUtils;

public class OrderDao extends Dao<Order> {
    private static final String TABLE_NAME = "pedido";

    private static final String COLUMN_ID = "cod_pedido";
    private static final String COLUMN_DELIVERY = "entregador_cod_entregador";
    private static final String COLUMN_ADDRESS = "endereco_cod_endereco";
    private static final String COLUMN_ACCOUNT_ID = "cliente_cpf";
    private static final String COLUMN_STATUS = "situacao";
    private static final String COLUMN_PRICE = "valor";
    private static final String COLUMN_TIMESTAMP = "data_pedido";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_DELIVERY = 2;
    private static final int IND_COLUMN_ADDRESS = 3;
    private static final int IND_COLUMN_ACCOUNT_ID = 4;
    private static final int IND_COLUMN_STATUS = 5;
    private static final int IND_COLUMN_PRICE = 6;
    private static final int IND_COLUMN_TIMESTAMP = 7;

	public OrderDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<Order> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_DELIVERY + ", " +
            		COLUMN_ADDRESS + ", " +
            		COLUMN_ACCOUNT_ID + ", " +
            		COLUMN_STATUS + ", " +
            		COLUMN_PRICE + ", " +
            		COLUMN_TIMESTAMP + ") " +
                    "VALUES (?,?,?,?,?,?)");

            for (Order ord : objectsToInsert) {
            	if (ord.getDeliveryGuyId() != SQLUtils.INVALID_ID) {
            		stm.setInt(1, ord.getDeliveryGuyId());
            	} else {
            		stm.setNull(1,java.sql.Types.INTEGER);
            	}

        		stm.setInt(2, ord.getAddressId());
        		stm.setLong(3, ord.getUserAccountId());
        		stm.setInt(4, ord.getStatus());
        		stm.setDouble(5, ord.getPrice());

        		Timestamp timestamp = new Timestamp(ord.getTimestamp().getTimeInMillis());
        		stm.setTimestamp(6, timestamp);

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar pedido!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(Order objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<Order> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Order> get(Order param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<Order> orders = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            orders = new ArrayList<Order>();
            while(rs.next()) {
            	Order o = new Order();
            	o.setId(rs.getLong(IND_COLUMN_ID));
            	o.setAddressId(rs.getInt(IND_COLUMN_ADDRESS));
            	o.setDeliveryGuyId(rs.getInt(IND_COLUMN_DELIVERY));
            	o.setStatus(rs.getInt(IND_COLUMN_STATUS));
            	o.setPriceFromQuery(rs.getDouble(IND_COLUMN_PRICE));
            	o.setUserAccountId(rs.getLong(IND_COLUMN_ACCOUNT_ID));

            	Timestamp ts = rs.getTimestamp(IND_COLUMN_TIMESTAMP);
            	Calendar cTs = Calendar.getInstance();
            	cTs.setTimeInMillis(ts.getTime());
            	o.setTimestamp(cTs);

                orders.add(o);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar produtos", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return orders;
	}

    private String buildQuery(Order param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getUserAccountId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ACCOUNT_ID + " = " + param.getUserAccountId());
                nextToken = and;
            }
            if (param.getId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getId());
                nextToken = and;
            }
            if (param.getDeliveryGuyId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_DELIVERY + " = " + param.getDeliveryGuyId());
                nextToken = and;
            }
            // Verifica se ha uma lista de status para buscar
            if (param.getQueryStatusSet() != null && param.getQueryStatusSet().size() > 0) {
            	HashSet<Integer> statusSet = param.getQueryStatusSet();
            	if (statusSet.size() > 0) {
            		queryBuilder.append(nextToken);
                    queryBuilder.append(" " + COLUMN_STATUS + " IN (");

                    boolean first = true;
                    for (int status : statusSet) {
                    	if (first) {
                    		first = false;
                    	} else {
                    		queryBuilder.append(", ");
                    	}
                    	queryBuilder.append(status);
                    }

                    queryBuilder.append(")");
            	}
            } else if (param.getQueryExcludeStatusSet() != null && param.getQueryExcludeStatusSet().size() > 0) {
            	HashSet<Integer> statusSet = param.getQueryExcludeStatusSet();
            	if (statusSet.size() > 0) {
            		queryBuilder.append(nextToken);
                    queryBuilder.append(" " + COLUMN_STATUS + " NOT IN (");

                    boolean first = true;
                    for (int status : statusSet) {
                    	if (first) {
                    		first = false;
                    	} else {
                    		queryBuilder.append(", ");
                    	}
                    	queryBuilder.append(status);
                    }

                    queryBuilder.append(")");
            	}
            } else if (param.hasValidState()) {
            	// Uma query nao deveria ser feita dessa forma mas,
            	// caso nao existam "statuses" no set de status, iremos utilizar
            	// o proprio campo status do pedido (caso seja valido)
        		queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_STATUS + " = " + param.getStatus());
            }
        }

        // Ordena pelo timestamp
        queryBuilder.append(" ORDER BY " + COLUMN_TIMESTAMP + " DESC");

        return queryBuilder.toString();
    }

    public void changeOrderStatus(int oldStatus, int newStatus, long orderId, int deliveryGuyId) throws SQLException {
    	checkOrderStatusTransition(oldStatus, newStatus);
    	Statement stm = null;
        try {
            stm = mConnection.createStatement();
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE " + TABLE_NAME + " SET " + COLUMN_STATUS + "=" + newStatus);
            if (deliveryGuyId > 0) {
            	builder.append(", " + COLUMN_DELIVERY + "=" + deliveryGuyId);
            }
            builder.append(" WHERE " + COLUMN_ID + "=" + orderId);
            stm.executeUpdate(builder.toString());

            LongPollingUtils.notifyOrderChange();
            if (newStatus == Order.OrderStatus.FINISHED) {
            	// TODO trigger email para clientes (sistema de recomendacao)
            }
        } catch (SQLException e) {
            Logger.error("Erro ao atualizar status de pedido", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }

    }

    private void checkOrderStatusTransition(int oldStatus, int newStatus) {
    	int expected = Order.OrderStatus.getNextAllowedState(oldStatus);
    	if (newStatus != expected) {
    		if (newStatus == Order.OrderStatus.READY_TO_PREPARE &&
    				oldStatus == Order.OrderStatus.WAITING_FOR_PAYMENT) {
    			return;
    		}
    		throw new IllegalStateException("Nao e possivel mover de " + oldStatus + " para " + newStatus + " Expected=" + expected);
    	}
    }

	@Override
	public long getLastSavedId() throws SQLException {
        long id = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME);
            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (SQLException e) {
            Logger.error("OrderDao.getLastSavedId", e);
            throw e;
        }
        return id;
	}

}
