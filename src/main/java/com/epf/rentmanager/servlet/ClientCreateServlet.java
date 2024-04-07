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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/users/create")
public class ClientCreateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        Client client = new Client(0, nom, prenom, email, naissance);

        if (client.getAge() < 18) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Client must be at least 18 years old.");
            return;
        }
        try {
            if (clientService.findByEmail(email).isPresent()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Un client avec cet email existe déjà.");
                return;
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error checking the email.");
            return;
        }

        try {
            clientService.create(client);
            response.setCharacterEncoding("UTF-8");
            response.sendRedirect(request.getContextPath() + "/users");
        } catch (ServiceException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating the client.");
        }
    }

}