// student id = 22059189
// Abstract Class: Vehicle
public abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (baseRentalRate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public void setBaseRentalRate(double baseRentalRate) {
        if (baseRentalRate < 0) throw new IllegalArgumentException("Rate cannot be negative");
        this.baseRentalRate = baseRentalRate;
    }

    public boolean isAvailableForRental() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double calculateRentalCost(int days);

    @Override
    public String toString() {
        return String.format("%s (%s): $%.2f/day", model, vehicleId, baseRentalRate);
    }
}

// Concrete Class: Car
class Car extends Vehicle implements Rentable {
    private boolean hasAirConditioning;
    private int seatCapacity;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasAirConditioning, int seatCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.hasAirConditioning = hasAirConditioning;
        this.seatCapacity = seatCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = days * getBaseRentalRate();
        if (hasAirConditioning) cost += days * 5; // Extra charge for AC
        return cost;
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) throw new IllegalStateException("Car is not available");
        setAvailable(false);
        System.out.printf("Car rented by %s for %d days. Total cost: $%.2f%n", customer.getName(), days, calculateRentalCost(days));
    }

    @Override
    public void returnVehicle() {
        setAvailable(true);
        System.out.println("Car returned and is now available.");
    }
}

// Concrete Class: Motorcycle
class Motorcycle extends Vehicle implements Rentable {
    private int engineCapacity;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, int engineCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.engineCapacity = engineCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return days * getBaseRentalRate() * (engineCapacity > 500 ? 1.2 : 1.0);
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) throw new IllegalStateException("Motorcycle is not available");
        setAvailable(false);
        System.out.printf("Motorcycle rented by %s for %d days. Total cost: $%.2f%n", customer.getName(), days, calculateRentalCost(days));
    }

    @Override
    public void returnVehicle() {
        setAvailable(true);
        System.out.println("Motorcycle returned and is now available.");
    }
}

// Concrete Class: Truck
class Truck extends Vehicle implements Rentable {
    private double cargoCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double cargoCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return days * getBaseRentalRate() + (cargoCapacity * 0.5);
    }

    @Override
    public void rent(Customer customer, int days) {
        if (!isAvailableForRental()) throw new IllegalStateException("Truck is not available");
        setAvailable(false);
        System.out.printf("Truck rented by %s for %d days. Total cost: $%.2f%n", customer.getName(), days, calculateRentalCost(days));
    }

    @Override
    public void returnVehicle() {
        setAvailable(true);
        System.out.println("Truck returned and is now available.");
    }
}

// Rentable Interface
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// Customer Class
class Customer {
    private String customerId;
    private String name;
    private List<RentalTransaction> rentalHistory;
    private List<Vehicle> currentRentals;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.rentalHistory = new ArrayList<>();
        this.currentRentals = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addRentalTransaction(RentalTransaction transaction) {
        rentalHistory.add(transaction);
    }

    public void addCurrentRental(Vehicle vehicle) {
        currentRentals.add(vehicle);
    }

    public void returnRental(Vehicle vehicle) {
        currentRentals.remove(vehicle);
    }
}

// RentalTransaction Class
class RentalTransaction {
    private String transactionId;
    private Vehicle vehicle;
    private Customer customer;
    private int rentalDays;
    private double totalCost;

    public RentalTransaction(String transactionId, Vehicle vehicle, Customer customer, int rentalDays, double totalCost) {
        this.transactionId = transactionId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDays = rentalDays;
        this.totalCost = totalCost;
    }
}

// RentalAgency Class
class RentalAgency {
    private List<Vehicle> fleet;
    private List<Customer> customers;

    public RentalAgency() {
        this.fleet = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void processRental(Customer customer, Vehicle vehicle, int days) {
        if (!vehicle.isAvailableForRental()) {
            System.out.println("Vehicle is not available for rental.");
            return;
        }

        vehicle.rent(customer, days);
        double cost = vehicle.calculateRentalCost(days);
        RentalTransaction transaction = new RentalTransaction(UUID.randomUUID().toString(), vehicle, customer, days, cost);
        customer.addRentalTransaction(transaction);
        customer.addCurrentRental(vehicle);
    }

    public void returnVehicle(Customer customer, Vehicle vehicle) {
        vehicle.returnVehicle();
        customer.returnRental(vehicle);
    }

    public void generateReport() {
        System.out.println("Rental Agency Report:");
        for (Vehicle vehicle : fleet) {
            System.out.println(vehicle);
        }
    }
}
