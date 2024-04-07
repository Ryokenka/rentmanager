package com.epf.rentmanager.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.models.Reservation;
import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;


@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Fetch the list of vehicles and users from the service classes
            List<Vehicule> vehicles = vehicleService.findAll();
            List<Client> clients = clientService.findAll();

            // Set them as attributes in the request object
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("clients", clients);

            // Forward to the JSP for creating reservations
            request.getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data from the database");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve parameters from the request
            int clientId = Integer.parseInt(request.getParameter("clientId"));
            int vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
            String debutStr = request.getParameter("begin");
            String finStr = request.getParameter("end");

            // Log the date strings to help diagnose any issues
            System.out.println("Debut: " + debutStr);
            System.out.println("Fin: " + finStr);

            if (debutStr == null || debutStr.isEmpty() || finStr == null || finStr.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Veuillez entrer des dates valides pour la réservation.");
                return;
            }
            LocalDate debut = LocalDate.parse(debutStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate fin = LocalDate.parse(finStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (debut.isAfter(fin.minusDays(7))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La réservation doit être d'au moins 7 jours.");
                return;
            }
            if (!reservationService.checkVehicleAvailability(vehicleId, debut, fin)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le véhicule n'est pas disponible pour cette période.");
                return;
            }

            // Create a new Reservation object
            Reservation reservation = new Reservation(clientId, vehicleId, debut, fin);

            // Call the service method to add the reservation
            reservationService.create(reservation);

            // Redirect to the list of reservations after successful creation
            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (NumberFormatException | ServiceException e) {
            // Handle exceptions (e.g., invalid input or service error)
            e.printStackTrace(); // You may want to log the exception
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating reservation");
        }
    }
}
