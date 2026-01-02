package de.hka.iwii.db1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Pattern( 
		regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
		message = "Must contain at least one letter and one number"
	)
	private String number;
	
	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;
	
	@Column(name = "start_airport", nullable = false)
	private String startAirport;
	
	// getters and setters
	
	public String getNumber() { return this.number; }
	public void setNumber(String number) { this.number = number; }
	
	public LocalDateTime getStartTime() { return this.startTime; }
	public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
	
	public String getStartAirport() { return this.startAirport; }
	public void setStartAirport(String startAirport) { this.startAirport = startAirport; }
}
