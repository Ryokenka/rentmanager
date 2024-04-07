package epf;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.models.Client;
import com.epf.rentmanager.service.ClientService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;


    private final ReservationService reservationService = mock(ReservationService.class);


    @Test
    public void testCreateClient() throws ServiceException, DaoException {
        Client client = new Client();
        client.setNom("Doe");
        client.setPrenom("John");

        when(clientDao.create(client)).thenReturn(1L);

        long result = clientService.create(client);

        assertEquals(1L, result);
    }

    @Test
    public void testCreateClientWithEmptyName() {
        Client client = new Client();
        client.setNom("");
        client.setPrenom("John");

        assertThrows(ServiceException.class, () -> clientService.create(client));
    }

    @Test
    public void testCreateClientWithEmptyFirstName() {
        Client client = new Client();
        client.setNom("Doe");
        client.setPrenom("");

        assertThrows(ServiceException.class, () -> clientService.create(client));
    }

}

