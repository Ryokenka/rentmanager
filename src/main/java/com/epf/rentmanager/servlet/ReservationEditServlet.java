package com.epf.rentmanager.servlet;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

@WebServlet("/rents/edit")
public class ReservationEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the reservation ID from the request parameter
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Reservation ID parameter is missing");
            return;
        }

        // Parse the reservation ID from the request parameter
        long reservationId;
        try {
            reservationId = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid reservation ID parameter");
            return;
        }

        try {
            // Retrieve the reservation details from the service
            Reservation reservation = reservationService.findById(reservationId);

            // Retrieve the lists of clients and vehicles from the service
            List<Client> clients = clientService.findAll();
            List<Vehicule> vehicles = vehicleService.findAll();

            // Set reservation, clients, and vehicles as attributes in the request scope
            request.setAttribute("reservation", reservation);
            request.setAttribute("clients", clients);
            request.setAttribute("vehicles", vehicles);

            // Format the dates retrieved from the database
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDebut = reservation.getDebut().format(formatter);
            String formattedFin = reservation.getFin().format(formatter);

            // Set the formatted dates in the request attribute
            request.setAttribute("formattedDebut", formattedDebut);
            request.setAttribute("formattedFin", formattedFin);

            // Forward to the JSP for editing reservations
            request.getRequestDispatcher("/WEB-INF/views/rents/edit.jsp").forward(request, response);
        } catch (ServiceException e) {
            // Handle service exceptions
            e.printStackTrace(); // Log the exception for debugging
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving reservation details");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve parameters from the request
        long reservationId = Long.parseLong(request.getParameter("id"));
        int clientId = Integer.parseInt(request.getParameter("client_id"));
        int vehicleId = Integer.parseInt(request.getParameter("vehicle_id"));
        String debutStr = request.getParameter("debut");
        String finStr = request.getParameter("fin");

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
        try {
            // Create a new Reservation object with updated details
            Reservation updatedReservation = new Reservation((int) reservationId, clientId, vehicleId, debut, fin);

            // Call the service method to update the reservation
            reservationService.update(updatedReservation);

            // Redirect to the list of reservations after successful update
            response.sendRedirect(request.getContextPath() + "/rents");
        } catch (ServiceException e) {
            // Handle service exceptions
            e.printStackTrace(); // Log the exception for debugging
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating reservation");
        }
    }
}

