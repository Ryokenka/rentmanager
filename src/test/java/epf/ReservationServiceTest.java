package epf;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.models.Reservation;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.ServiceException;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationDao reservationDao;


    @Test
    public void testCreateReservation() throws ServiceException, DaoException {
        Reservation reservation = new Reservation();

        when(reservationDao.create(reservation)).thenReturn(1L);

        long result = reservationService.create(reservation);

        assertEquals(1L, result);
    }

    @Test
    public void testCreateReservationFailure() throws DaoException {
        Reservation reservation = new Reservation();
        // Set reservation properties

        when(reservationDao.create(reservation)).thenThrow(new DaoException("Test Exception"));

        assertThrows(ServiceException.class, () -> reservationService.create(reservation));
    }

    @Test
    public void testFindAllReservations() throws ServiceException, DaoException {
        List<Reservation> reservations = new ArrayList<>();
        // Add reservations to the list

        when(reservationDao.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertEquals(reservations.size(), result.size());
    }

}

