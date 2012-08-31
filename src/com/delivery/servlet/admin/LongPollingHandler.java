package com.delivery.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.util.LongPollingUtils;

public class LongPollingHandler extends HttpServlet {

	private static final long serialVersionUID = -5487099535987927365L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Logger.debug("LongPollingHandler iniciado!");
		LongPollingUtils.LongPollingLock lock = new LongPollingUtils.LongPollingLock();
		LongPollingUtils.registerOrderLPLock(lock);
		boolean interrupted = false;
		synchronized(lock) {
			try {
				lock.wait(60000);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
		LongPollingUtils.unregisterOrderLPLock(lock);
		Logger.debug("LongPollingHandler sendo finalizado! - Interrupted: " + interrupted + " timedOut: " + lock.timedOut);

		if (interrupted || lock.timedOut) {
			resp.getOutputStream().print("var shouldLoad=false");
		} else {
			resp.getOutputStream().print("var shouldLoad=true");
		}

	}

}
