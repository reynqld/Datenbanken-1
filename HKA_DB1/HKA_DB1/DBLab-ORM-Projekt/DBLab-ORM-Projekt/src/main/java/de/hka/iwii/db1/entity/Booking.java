package de.hka.iwii.db1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
	private Flight flight;
	
	@Column(name = "number_of_seats")
	@Min(1)
	private int seats;
	
	@Column(name = "booking_date", nullable = false)
	private LocalDate bookingDate;
	
	// getters and setters

	public Customer getCustomer() { return this.customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	
	public Flight getFlight() { return this.flight; }
	public void setFlight(Flight flight) { this.flight = flight; }
	
	public int getSeats() { return this.seats; }
	public void setSeats(int seats) { this.seats = seats; }
	
	public LocalDate getBookingDate() { return this.bookingDate; }
	public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
	
}
