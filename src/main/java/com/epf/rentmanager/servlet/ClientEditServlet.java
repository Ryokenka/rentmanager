package com.epf.rentmanager.servlet;

import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/users/edit")
public class ClientEditServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Client client = clientService.findById(id);
            if (client != null) {
                // format birthdate as string in yyyy-MM-dd format
                String birthdate = (client.getNaissance() != null) ? client.getNaissance().format(DateTimeFormatter.ISO_DATE) : "";
                request.setAttribute("client", client);
                request.setAttribute("birthdate", birthdate);
                request.getRequestDispatcher("/WEB-INF/views/users/edit.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found.");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching the client.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nom = request.getParameter("last_name");
        String prenom = request.getParameter("first_name");
        String email = request.getParameter("email");
        String naissanceStr = request.getParameter("birthdate");

        if (nom.length() < 3 || prenom.length() < 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le nom et le prénom doivent contenir au moins 3 caractères.");
            return;
        }

        if (naissanceStr == null || naissanceStr.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Date of birth is required.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate naissance = LocalDate.parse(naissanceStr, formatter);

        Client client = new Client(id, nom, prenom, email, naissance);

        if (client.getAge() < 18) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client must be at least 18 years old.");
            return;
        }

        try {
            if (clientService.findByEmail(email).isPresent() && clientService.findByEmail(email).get().getId() != id) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Un client avec cet email existe déjà.");
                return;
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking the email.");
            return;
        }

        try {
            clientService.update(client);
            response.setCharacterEncoding("UTF-8");
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating the client.");
        }
    }
}

