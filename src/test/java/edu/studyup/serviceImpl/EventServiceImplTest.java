package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	
	
	//Test cases that we write
	//I pulled all the bug cases up front to have better look
	
	//First failure in Update event name
	//Put 20 characters which is the MAX, should not have an error
	@Test
	void testUpdateEventName_sizeisMax_bad() throws StudyUpException {
		int eventID = 1;
		assertEquals("ssssswwwwwqqqqqeeeee", 
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "ssssswwwwwqqqqqeeeee");
		  }));
		//should output ssssswwwwwqqqqqeeeee because 20 is a valid size
	}
	
	//Second failure in get active events
	// one bug found in Get Active Events, it should not get past events
	// only active events should be collected, not past events
	@Test
	void testGetActiveEvents_old_bad() {
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(2000));
		event2.setName("Event 2");
		Location location = new Location(-120, 35);
		event2.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		event2.setStudents(eventStudents);
		DataStorage.eventData.put(event2.getEventID(), event2);
		eventServiceImpl.getActiveEvents();
		assertFalse(eventServiceImpl.getActiveEvents().contains(event2));
	}

	//Third failure in add students to event
	//One bug in Add student to Event
	//We can't have more than two students in the event
	//3rd student failed to add into the event
	@Test
	void testAddstudentsToEvent_twostudents_bad() throws StudyUpException{
		int eventID=1;
		Student student2 = new Student();
		student2.setFirstName("Zheng");
		student2.setLastName("Xu");
		student2.setEmail("adxu@email.com");
		student2.setId(2);
		Student student3 = new Student();
		student3.setFirstName("Weili");
		student3.setLastName("Yin");
		student3.setEmail("ywl@email.com");
		student3.setId(3);
		eventServiceImpl.addStudentToEvent(student3, eventID);
		assertEquals(3,DataStorage.eventData.get(eventID).getStudents().size());
	}	
	
	
	
	
	
	
	
	
	//Update Event Name Test
	//over 20 length 
	@Test
	void testUpdateEventName_LengthTooLong() throws StudyUpException {
		int eventID=1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "sssssssssssssssssssssssssssssssssssss");
		  });
	}
	
	
	
	//Get active events Test
	@Test
	void testGetActiveEvents() {
		eventServiceImpl.getActiveEvents();
		assertEquals(1, DataStorage.eventData.size());
	}
	
	
	
	//Get past events Test
	@Test
	void testGetPastEvents() {
		eventServiceImpl.getPastEvents();
		assertEquals(1, DataStorage.eventData.size());
	}
	
	//Check if it get the old events
	@Test
	void testGetPastEvents_oldevents() {
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(1990));
		event2.setName("Event 2");
		Location location = new Location(-120, 35);
		event2.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		event2.setStudents(eventStudents);
		DataStorage.eventData.put(event2.getEventID(), event2);
		eventServiceImpl.getPastEvents();
		assertTrue(eventServiceImpl.getActiveEvents().contains(event2));
	}
	
	
	
	//Add Student to Event Test
	//If the Event is NULL
	@Test
	void testAddStudentsToEvent_NullEvent() {
		int eventID=2;
		Student student2 = new Student();
		student2.setFirstName("Zheng");
		student2.setLastName("Xu");
		student2.setEmail("adxu@email.com");
		student2.setId(2);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student2, eventID);
		  });
	}
	
	//If only add one student
	@Test
	void testAddstudentsToEvent_onestudent_good() throws StudyUpException{
		int eventID=1;
		Student student2 = new Student();
		student2.setFirstName("Zheng");
		student2.setLastName("Xu");
		student2.setEmail("adxu@email.com");
		student2.setId(2);
		eventServiceImpl.addStudentToEvent(student2, eventID);
		assertEquals(2,DataStorage.eventData.get(eventID).getStudents().size());
	}

	//present student is NULL
	@Test
	void testAddstudentsToEvent_presentstudentnull() throws StudyUpException{
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date());
		event2.setName("Event 2");
		DataStorage.eventData.put(event2.getEventID(), event2);
		Student student2 = new Student();
		student2.setFirstName("Zheng");
		student2.setLastName("Xu");
		student2.setEmail("adxu@email.com");
		student2.setId(2);
		eventServiceImpl.addStudentToEvent(student2, 2);
		assertEquals(1,DataStorage.eventData.get(2).getStudents().size());
	}

	
	
	//Delete event Test
	@Test
	void testDeleteEvent () {
		int eventID=1;
		eventServiceImpl.deleteEvent(eventID);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Deleted Event");
		  });
	}
}
