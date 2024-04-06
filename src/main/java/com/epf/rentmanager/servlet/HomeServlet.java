package com.epf.rentmanager.servlet;

import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	@Autowired
	VehicleService vehicleService;
	@Autowired
	ClientService clientService;
	@Autowired
	ReservationService reservationService;
	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

	}
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int count = vehicleService.countVehicles();
			request.setAttribute("vehicleCount", count);
			int ccount = clientService.countClients();
			request.setAttribute("clientCount", ccount);
			int rcount = reservationService.count();
			request.setAttribute("reservationCount", rcount);
		} catch (ServiceException e) {
			throw new ServletException(e);
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);

	}

}