package edu.franklin.comp671.ems.service;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.ValidationException;

import edu.franklin.comp671.ems.models.Event;

public class EventManager {

	static Map<String, Event> events = new HashMap<String, Event>();

	public void createEvent(Event event) {

		// Check whether an event already exists
		if (!events.containsKey(event.getName())) {
			events.put(event.getName(), event);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public String updateEvent(String eventName, Event event) throws ValidationException {

		if (events.containsKey(eventName)) {
			// Overwrite the event in HashMap
			events.remove(eventName);
			events.put(event.getName(), event);

		} else {
			throw new ValidationException("Event not found");
		}

		return null;
	}

	public String cancelEvent(String eventName) throws ValidationException {

		if (events.containsKey(eventName)) {
			Event event = events.get(eventName);
			if (isEventDateExpired(event.getDate())) {
				throw new ValidationException("Event cannot be canceled");
			} else {
				// remove the event name
				events.remove(eventName);
			}

		} else {
			throw new ValidationException("Event not found");
		}
		return null;
	}

	public Event getEvent(String name) {

		return events.get(name);
	}

	private boolean isEventDateExpired(String date) {
		boolean result = false;
		SimpleDateFormat sdfo = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Format f = new SimpleDateFormat("MM/dd/yyyy");
			String strDate = f.format(new Date());
			System.out.println(strDate);
			Date currentDate = sdfo.parse(strDate);
			Date eventDate = sdfo.parse(date);
			if (currentDate.compareTo(eventDate) > 0 || currentDate.compareTo(eventDate) == 0) {
				result = true;
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return result;

	}

}
