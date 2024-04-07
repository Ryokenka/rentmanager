package com.epf.rentmanager.servlet;

import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/delete")
public class ClientDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long vehicleId = Long.parseLong(request.getParameter("id"));

        try {
            // Find the Vehicle object corresponding to the vehicleId
            Client vehicle = clientService.findById(vehicleId);

            // Call the delete method with the found Vehicle object
            clientService.delete(vehicle);

            response.sendRedirect(request.getContextPath() + "/users");
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting the user.");
        }
    }

}
