package com.delivery.admin.deliveryguy;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.engine.command.DeliveryGuyManagerCommand;
import com.delivery.order.DeliveryGuy;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.DeliveryGuyDao;
import com.delivery.util.StringUtils;

public class InsertDeliveryGuyCmd extends DeliveryGuyManagerCommand {
	private String mRedirect;

	@Override
	public void execute(HttpServletRequest request) {
        mRedirect = "DeliveryGuyList.jsp";
		try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            String name = request.getParameter("name");
            String pwd = request.getParameter("pwd");

            if ((StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd))) {
            	return;
            }
            final DeliveryGuy deliveryGuy = new DeliveryGuy();
            deliveryGuy.setName(name);
            deliveryGuy.setPassword(pwd);

            DaoManager daoManager = new DaoManager(dataSource);
            @SuppressWarnings("unchecked")
			List<DeliveryGuy> result = (List<DeliveryGuy>) daoManager.transaction(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					DeliveryGuyDao dao = manager.getDeliveryGuyDao();
					int code = (int) dao.getNextSequenceVal();
					deliveryGuy.setCode(code);
					ArrayList<DeliveryGuy> toInsert = new ArrayList<DeliveryGuy>();
					toInsert.add(deliveryGuy);
					dao.save(toInsert);

					return dao.get(null);
				}
			});
            request.setAttribute("DeliveryGuyList", result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
