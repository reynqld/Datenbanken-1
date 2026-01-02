package de.hka.iwii.db1.jpa;

import de.hka.iwii.db1.entity.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.time.LocalDateTime;
import java.time.LocalDate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class JPAApplication {
	private EntityManagerFactory entityManagerFactory;

	public JPAApplication() {
		Logger.getLogger("org.hibernate").setLevel(Level.ALL);
		entityManagerFactory = Persistence.createEntityManagerFactory("DB1");
	}
	
	private Customer addCustomer(String firstName, String lastName, String email) {
		Customer newCustomer = new Customer();
		newCustomer.setFirstName(firstName);
		newCustomer.setLastName(lastName);
		newCustomer.setEmail(email);
		return newCustomer;
	}
	
	private Flight addFlight(String number, LocalDateTime startTime, String startAirport) {
		Flight newFlight = new Flight();
		newFlight.setNumber(number);
		newFlight.setStartAirport(startAirport);
		newFlight.setStartTime(startTime);
		return newFlight;
	}
	
	private Booking addBooking(Customer customer, Flight flight, int seats, LocalDate bookingDate) {
		Booking newBooking = new Booking();
		newBooking.setCustomer(customer);
		newBooking.setFlight(flight);
		newBooking.setSeats(seats);
		newBooking.setBookingDate(bookingDate);
		return newBooking;
	}
	
	public void testFlights() {
		EntityManagerFactory emf = (new JPAApplication()).getEntityManagerFactory();
		
		// add customers
		
		EntityManager emCustomers = emf.createEntityManager();
		emCustomers.getTransaction().begin();
		
		Customer newCustomer1 = addCustomer("Daniel", "Schreiber", "danielschreiber@gmail.com");
		Customer newCustomer2 = addCustomer("Chris", "Schoebel", "chrisschoebel@gmail.com");
		emCustomers.persist(newCustomer1);
		emCustomers.persist(newCustomer2);
		
		emCustomers.getTransaction().commit();
		emCustomers.close();
		
		// add flights
		
		EntityManager emFlights = emf.createEntityManager();
		emFlights.getTransaction().begin();
		
	    Flight newFlight1 = addFlight("LH1234", LocalDateTime.of(2026, 3, 15, 8, 30), "Frankfurt Airport (FRA)");
		Flight newFlight2 = addFlight("EW987", LocalDateTime.of(2026, 3, 15, 14, 45), "Munich Airport (MUC)");
		Flight newFlight3 = addFlight("DE4501", LocalDateTime.of(2026, 3, 16, 6, 10), "Berlin Brandenburg Airport (BER)");
		emFlights.persist(newFlight1);
		emFlights.persist(newFlight2);
		emFlights.persist(newFlight3);
		
		emFlights.getTransaction().commit();
		emFlights.close();
		
		// add bookings
		
		EntityManager emBookings = emf.createEntityManager();
		emBookings.getTransaction().begin();
		
		Flight flight1 = emBookings.find(Flight.class, 1);
		Flight flight2 = emBookings.find(Flight.class, 2);
		Flight flight3 = emBookings.find(Flight.class, 3);
		Customer customer1 = emBookings.find(Customer.class, 1);
		Customer customer2 = emBookings.find(Customer.class, 2);
		
		Booking newBooking1 = addBooking(customer1, flight1, 2, LocalDate.now());
		Booking newBooking2 = addBooking(customer1, flight2, 2, LocalDate.now());
	
		Booking newBooking3 = addBooking(customer2, flight2, 2, LocalDate.of(2026, 1, 7));
		Booking newBooking4 = addBooking(customer2, flight3, 2, LocalDate.of(2026, 1, 7));
		
		emBookings.persist(newBooking1);
		emBookings.persist(newBooking2);
		emBookings.persist(newBooking3);
		emBookings.persist(newBooking4);
		
		emBookings.getTransaction().commit();
		emBookings.close();
		
		// read out bookings of a customer
		
		EntityManager emRead = emf.createEntityManager();
		emRead.getTransaction().begin();
		
		TypedQuery<Booking> query = emRead.createQuery("FROM Booking b JOIN b.customer c WHERE c.lastName = :lastName",
				 						 			   Booking.class);
		String lastName = "Schoebel";
		query.setParameter("lastName", lastName);
		List<Booking> bookings = query.getResultList();
		
		System.out.println("\n\n\nCustomer: " + lastName + "\n");
		int counter = 1;
		for (Booking booking: bookings) {
			System.out.println(counter + ".\n" + 
							   "Flight ID: " + booking.getFlight().getNumber() + 
							   "\nBooking Date: " + booking.getBookingDate() + 
							   "\nNumber of Seats: " + booking.getSeats() + "\n");
			counter++;
		}
		
		emRead.close();		
		
		emf.close();
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
	
	public static void main(String[] args) {
		JPAApplication app = new JPAApplication();
		app.testFlights();
	}
}
