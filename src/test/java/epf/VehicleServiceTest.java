package epf;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.epf.rentmanager.dao.DaoException;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.models.Vehicule;
import com.epf.rentmanager.service.ServiceException;
import com.epf.rentmanager.service.VehicleService;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleDao vehicleDao;


    @Test
    public void testCreateVehicle() throws ServiceException, DaoException {
        Vehicule vehicle = new Vehicule();
        vehicle.setConstructeur("Toyota");
        vehicle.setNb_places(5);

        when(vehicleDao.create(vehicle)).thenReturn(1L);

        long result = vehicleService.create(vehicle);

        assertEquals(1L, result);
    }

    @Test
    public void testCreateVehicleWithEmptyManufacturer() {
        Vehicule vehicle = new Vehicule();
        vehicle.setConstructeur("");
        vehicle.setNb_places(5);

        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));
    }

    @Test
    public void testCreateVehicleWithZeroSeats() {
        Vehicule vehicle = new Vehicule();
        vehicle.setConstructeur("Toyota");
        vehicle.setNb_places(0);

        assertThrows(ServiceException.class, () -> vehicleService.create(vehicle));
    }

    @Test
    public void testFindAllVehicles() throws ServiceException, DaoException {
        List<Vehicule> vehicles = new ArrayList<>();
        vehicles.add(new Vehicule("Toyota", "Yaris", 5));
        vehicles.add(new Vehicule("Honda", "Jazz", 4));

        when(vehicleDao.findAll()).thenReturn(vehicles);

        List<Vehicule> result = vehicleService.findAll();

        assertEquals(2, result.size());
    }

}
