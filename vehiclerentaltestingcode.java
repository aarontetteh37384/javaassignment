student id = 22059189
public class VehicleRentalSystemTest {

    @Test
    public void testCarRentalCost() {
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        assertEquals(110.0, car.calculateRentalCost(2), 0.01, "Car rental cost calculation is incorrect");
    }

    @Test
    public void testMotorcycleRentalCost() {
        Motorcycle motorcycle = new Motorcycle("M001", "Honda CBR", 30.0, 600);
        assertEquals(72.0, motorcycle.calculateRentalCost(2), 0.01, "Motorcycle rental cost calculation is incorrect");
    }

    @Test
    public void testTruckRentalCost() {
        Truck truck = new Truck("T001", "Ford F-150", 100.0, 1000);
        assertEquals(350.0, truck.calculateRentalCost(2), 0.01, "Truck rental cost calculation is incorrect");
    }

    @Test
    public void testCarAvailability() {
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        assertTrue(car.isAvailableForRental(), "Car should initially be available");
        car.setAvailable(false);
        assertFalse(car.isAvailableForRental(), "Car availability not set correctly");
    }

    @Test
    public void testRentalTransaction() {
        Customer customer = new Customer("CU001", "John Doe");
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        RentalTransaction transaction = new RentalTransaction("T001", car, customer, 3, car.calculateRentalCost(3));

        assertEquals("T001", transaction.transactionId, "Transaction ID mismatch");
        assertEquals(customer, transaction.customer, "Customer mismatch");
        assertEquals(car, transaction.vehicle, "Vehicle mismatch");
        assertEquals(3, transaction.rentalDays, "Rental days mismatch");
        assertEquals(165.0, transaction.totalCost, 0.01, "Transaction total cost mismatch");
    }

    @Test
    public void testProcessRental() {
        RentalAgency agency = new RentalAgency();
        Customer customer = new Customer("CU001", "John Doe");
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        agency.addVehicle(car);
        agency.addCustomer(customer);

        agency.processRental(customer, car, 2);
        assertFalse(car.isAvailableForRental(), "Car should be unavailable after rental");
        assertEquals(1, customer.currentRentals.size(), "Customer rentals not updated correctly");
    }

    @Test
    public void testReturnVehicle() {
        RentalAgency agency = new RentalAgency();
        Customer customer = new Customer("CU001", "John Doe");
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        agency.addVehicle(car);
        agency.addCustomer(customer);

        agency.processRental(customer, car, 2);
        agency.returnVehicle(customer, car);

        assertTrue(car.isAvailableForRental(), "Car should be available after return");
        assertEquals(0, customer.currentRentals.size(), "Customer rentals not cleared after return");
    }

    @Test
    public void testInvalidRental() {
        Car car = new Car("C001", "Toyota Corolla", 50.0, true, 5);
        car.setAvailable(false);
        Customer customer = new Customer("CU001", "John Doe");

        Exception exception = assertThrows(IllegalStateException.class, () -> car.rent(customer, 2));
        assertEquals("Car is not available", exception.getMessage(), "Exception message mismatch");
    }
}
