package edu.franklin.comp671.ems.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import javax.xml.bind.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Nested;

import edu.franklin.comp671.ems.models.Account;
import edu.franklin.comp671.ems.models.Event;
import edu.franklin.comp671.ems.models.TicketPrice;
import edu.franklin.comp671.ems.service.EventManager;

//@TestMethodOrder(OrderAnnotation.class)
public class EventTest {

	static EventManager eventManager;

	@BeforeAll
	static void setUp() {
		eventManager = new EventManager();
	}

	@Nested
	@DisplayName("Create Event Test Cases")
	@TestMethodOrder(OrderAnnotation.class)
	class CreateEventTest {

		@Test
		@Order(1)
		@DisplayName("Create an event that does not exist")
		void create_non_existing_event() {
			Event event = new Event();
			event.setName("Graduation Party");
			event.setVenue("Columbus");
			event.setDate("03/01/2020");
			Assertions.assertDoesNotThrow(() -> eventManager.createEvent(event));
		}

		@Test
		@Order(2)
		@DisplayName("Create an event that exists")
		void create_existing_event() {
			Event event = new Event();
			event.setName("Graduation Party");
			event.setVenue("Columbus");
			event.setDate("03/01/2020");
			Assertions.assertThrows(IllegalArgumentException.class, () -> eventManager.createEvent(event));
		}

		@ParameterizedTest
		@MethodSource("edu.franklin.comp671.ems.tests.EventTest#eventObjectSource")
		@Order(3)
		@DisplayName("Create an event with parameters passed from another method")
		void create_event_with_parameters(String eventName, String venue, String eventDate) {
			Event event = new Event();
			event.setName(eventName);
			event.setVenue(venue);
			event.setDate(eventDate);
			Assertions.assertDoesNotThrow(() -> eventManager.createEvent(event));
		}

		@ParameterizedTest
		@MethodSource("edu.franklin.comp671.ems.tests.EventTest#eventObjectSource")
		@Order(4)
		@DisplayName("Create an event which already exists with parameters passed from another method")
		void create_event_with_parameters_already_saved(String eventName, String venue, String eventDate) {
			Event event = new Event();
			event.setName(eventName);
			event.setVenue(venue);
			event.setDate(eventDate);
			// Assert that Exception is thrown
			Assertions.assertThrows(IllegalArgumentException.class, () -> eventManager.createEvent(event));
		}

		@Test
		@Order(5)
		@DisplayName("Create an event with ticket price and validate whether the data has been saved")
		void create_an_event_with_ticket_price() {
			Event event = new Event();
			event.setName("Band1 Spring Tour");
			event.setVenue("Columbus");
			event.setDate("03/05/2020");
			TicketPrice price = new TicketPrice();
			price.setPrice(22.50);
			price.setCurrency("USD");
			;
			event.setTicketPrice(price);
			Assertions.assertDoesNotThrow(() -> eventManager.createEvent(event));
			Event savedEvent = eventManager.getEvent("Band1 Spring Tour");
			Assumptions.assumeTrue(savedEvent != null);
			Assertions.assertAll("savedEvent", () -> assertEquals("Band1 Spring Tour", savedEvent.getName()),
					() -> assertEquals("Columbus", savedEvent.getVenue()),
					() -> assertEquals(22.50, savedEvent.getTicketPrice().getPrice()));
		}

	}

	@Nested
	@DisplayName("Update Event Test Cases")
	@TestMethodOrder(OrderAnnotation.class)
	class UpdateEventTest {

		@BeforeEach
		void create_non_existing_event() {
			Event event = new Event();
			event.setName("Graduation Party 2");
			event.setVenue("Columbus");
			event.setDate("03/01/2020");
			eventManager.createEvent(event);
		}

		@Test
		@Order(1)
		@DisplayName("Update an event that exists and assert that it has been saved")
		void update_existing_event() {
			Event event = new Event();
			event.setName("Birthday Party");
			event.setVenue("Columbus");
			event.setDate("03/01/2020");

			Assertions.assertAll("event",
					() -> assertDoesNotThrow(() -> eventManager.updateEvent("Graduation Party 2", event)),
					() -> assertEquals("Birthday Party", eventManager.getEvent("Birthday Party").getName()));
			// Assertions.assertDoesNotThrow(() -> eventManager.updateEvent("Graduation
			// Party 2",event));
		}

	}

	@Nested
	@DisplayName("Cancel Event Test Cases")
	@TestMethodOrder(OrderAnnotation.class)
	@TestInstance(Lifecycle.PER_CLASS)
	class CancelEventTest {

		@BeforeAll
		void setup() {
			Event event = new Event();
			event.setName("Band2 Summer Tour");
			event.setVenue("Columbus");
			event.setDate("03/01/2020");
			eventManager.createEvent(event);
			Event event2 = new Event();
			event2.setName("Band3 Summer Tour");
			event2.setVenue("Columbus");
			event2.setDate("02/24/2020");
			eventManager.createEvent(event2);
		}

		@Test
		@Order(1)
		@DisplayName("Cancel Event with future date")
		void cancel_event_with_future_date() {

			Assertions.assertAll("event", () -> assertDoesNotThrow(() -> eventManager.cancelEvent("Band2 Summer Tour")),
					() -> assertNull(eventManager.getEvent("Band2 Summer Tour")));

		}

		@Test
		@Order(2)
		@DisplayName("Cancel Event with todays date")
		void cancel_event_with_todays_date() {

			Assertions.assertAll("event",
					() -> Assertions.assertThrows(ValidationException.class,
							() -> eventManager.cancelEvent("Band3 Summer Tour")),
					() -> assertNotNull(eventManager.getEvent("Band3 Summer Tour")));

		}

	}

	static Stream<Arguments> eventObjectSource() {

		return Stream.of(Arguments.arguments("Birthday Party", "Columbus", "03/01/2020"),
				Arguments.arguments("Testing Party", "Columbus", "03/01/2020"));

	}

}
