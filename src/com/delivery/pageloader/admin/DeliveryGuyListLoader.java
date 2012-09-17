package com.delivery.pageloader.admin;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.order.DeliveryGuy;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.DeliveryGuyDao;

public class DeliveryGuyListLoader extends AdminPageLoaderCommand {
	private String mRedirect;

	@Override
	public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
		Logger.debug("Preparing to load DeliveryGuyListLoader");
        mRedirect = "DeliveryGuyList.jsp";
		try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            DaoManager daoManager = new DaoManager(dataSource);
            @SuppressWarnings("unchecked")
			List<DeliveryGuy> list = (List<DeliveryGuy>) daoManager.execute(new DaoManager.DaoCommand() {
				@Override
				public Object execute(DaoManager manager) throws SQLException {
					DeliveryGuyDao dao = manager.getDeliveryGuyDao();
					return dao.get(null);
				}
			});

            req.setAttribute("DeliveryGuyList", list);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getRedirect() {
		return mRedirect;
	}

}
