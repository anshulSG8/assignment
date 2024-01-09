package com.interview.bt;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.interview.bt.model.Link;
import com.interview.bt.model.Network;
import com.interview.bt.model.NetworkElement;
import com.interview.bt.model.Person;

@SpringBootTest
class AssignmentApplicationTests {

	@InjectMocks
	private AssignmentApplication assignmentApplication;

	private List<Network> networks;

	private List<NetworkElement> networkElements;

	private List<Link> links;

	private List<Person> persons;

	@BeforeEach
	public void init() {
		networks = Arrays.asList(new Network("NE1", "NE2", "Link12"), new Network("NE3", "NE4", "Link34"));
		networkElements = Arrays.asList(new NetworkElement("NE1", "2", "Exchange1"),
				new NetworkElement("NE2", "3", "Exchange1"), new NetworkElement("NE4", "1", ""),
				new NetworkElement("NE3", "1", ""));
		links = Arrays.asList(new Link("Link12", "5"), new Link("Link23", "2"), new Link("Link34", "5"));
		persons = Arrays.asList(new Person("PersonA", "Exchange1"), new Person("PersonC", "Exchange1"),
				new Person("PersonB", "Exchange2"), new Person("PersonD", ""));
	}

	@Test
	public void checkIfLinkIsPresentOrNotInvalid() {
		Boolean result = ReflectionTestUtils.invokeMethod(assignmentApplication, "checkIfLinkIsPresentOrNot", "a", "b",
				networks);
		assertEquals(false, result);
	}

	@Test
	public void checkIfLinkIsPresentOrNotValid() {
		Boolean result = ReflectionTestUtils.invokeMethod(assignmentApplication, "checkIfLinkIsPresentOrNot", "NE1",
				"NE2", networks);
		assertEquals(true, result);
	}

	@Test
	public void getProcessingTimeTest() {
		Integer result = ReflectionTestUtils.invokeMethod(assignmentApplication, "getProcessingTime", networkElements,
				"NE2");
		assertEquals(3, result.intValue());
	}

	@Test
	public void calculatePriceTest() {
		Integer result = ReflectionTestUtils.invokeMethod(assignmentApplication, "calculatePrice", "Link12", "Link21",
				"NE1", "NE2", links, networkElements);
		assertEquals(35, result.intValue());
	}

	@Test
	public void generateInitialCostArrayTest() {
		int[][] arr = ReflectionTestUtils.invokeMethod(assignmentApplication, "generateInitialCostArray", networks,
				links, networkElements);
		int[][] result = { { 0, 0, 0, 0, 0 }, { 0, 0, 35, Integer.MAX_VALUE, Integer.MAX_VALUE },
				{ 0, 35, 0, Integer.MAX_VALUE, Integer.MAX_VALUE }, { 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 20 },
				{ 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, 0 } };

		assertArrayEquals(result, arr);
	}

	@Test
	public void updateConsideringNetworkElement2() {
		int[][] arr = { { 0, 0, 0, 0, 0 }, { 0, 0, 35, Integer.MAX_VALUE, Integer.MAX_VALUE },
				{ 0, 35, 0, Integer.MAX_VALUE, Integer.MAX_VALUE }, { 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 20 },
				{ 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, 0 } };

		ReflectionTestUtils.invokeMethod(assignmentApplication, "updateConsideringNetworkElement2", arr, networks);

	}

	@Test
	public void updateConsideringNetworkElement1() {
		int[][] arr = { { 0, 0, 0, 0, 0 }, { 0, 0, 35, Integer.MAX_VALUE, Integer.MAX_VALUE },
				{ 0, 35, 0, Integer.MAX_VALUE, Integer.MAX_VALUE }, { 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 20 },
				{ 0, Integer.MAX_VALUE, Integer.MAX_VALUE, 20, 0 } };

		ReflectionTestUtils.invokeMethod(assignmentApplication, "updateConsideringNetworkElement1", arr, networks);

	}

	@Test
	public void isValidSourceAndDestinationInvalid() {

		boolean result = ReflectionTestUtils.invokeMethod(assignmentApplication, "isValidSourceAndDestination",
				"PersonA1", "PersonB", persons);

		assertEquals(false, result);

	}

	@Test
	public void isValidSourceAndDestinationValid() {

		boolean result = ReflectionTestUtils.invokeMethod(assignmentApplication, "isValidSourceAndDestination",
				"PersonA", "PersonB", persons);

		assertEquals(true, result);

	}

}
