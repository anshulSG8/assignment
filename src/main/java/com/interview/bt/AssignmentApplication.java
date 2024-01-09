package com.interview.bt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.interview.bt.model.Link;
import com.interview.bt.model.Network;
import com.interview.bt.model.NetworkElement;
import com.interview.bt.model.Person;

@SpringBootApplication
public class AssignmentApplication {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		try (Scanner scanner = new Scanner(System.in)) {
			// get the source person name from user
			System.out.println("Enter source person name:");
			String sourcePersonName = scanner.nextLine(); // Read user input

			// get the destination person name from user
			System.out.println("Enter destination person name:");
			String destinationPersonName = scanner.nextLine(); // Read user input

			// validate if source and destination is same
			if (sourcePersonName.equalsIgnoreCase(destinationPersonName)) {
				System.out.println("Source and destination cannot be same");
				return;
			}

			// get list of all persons
			List<Person> persons = getPersons();

			// validate if source and destination exits or not
			if (isValidSourceAndDestination(sourcePersonName, destinationPersonName, persons)) {

				Optional<Person> sourceExchangeOptional = persons.stream()
						.filter(person -> person.getPersonName().equalsIgnoreCase(sourcePersonName)).findFirst();
				Optional<Person> destinationExchangeOptional = persons.stream()
						.filter(person -> person.getPersonName().equalsIgnoreCase(destinationPersonName)).findFirst();

				// check if either of the exchange is invalid
				if (sourceExchangeOptional.isEmpty() || destinationExchangeOptional.isEmpty()) {
					System.out.println("Exchange doesnt exists for source or destination");
					return;
				}

				Person sourcePerson = sourceExchangeOptional.get();
				Person destinationPerson = destinationExchangeOptional.get();

				findLeastCostRoute(sourcePerson, destinationPerson);
			} else {
				System.out.println("Invalid source or destination");
			}
		}

	}

	private static void findLeastCostRoute(Person sourcePerson, Person destinationPerson)
			throws FileNotFoundException, IOException {

		// get all links
		List<Link> links = getLinks();

		// get all networks
		List<Network> networks = getNetworks();

		// get all networkelements
		List<NetworkElement> networkElements = getNetworkElements();

		// get possible start networkelements
		List<String> possibleStartNetworkElements = networkElements.stream()
				.filter(networkElement -> networkElement.getExchange().equalsIgnoreCase(sourcePerson.getExchange()))
				.map(networkElement -> networkElement.getName()).collect(Collectors.toList());
		// get possible end networkelements
		List<String> possibleEndNetworkElements = networkElements.stream().filter(
				networkElement -> networkElement.getExchange().equalsIgnoreCase(destinationPerson.getExchange()))
				.map(networkElement -> networkElement.getName()).collect(Collectors.toList());

		// generate the initial cost array based on the given input
		int[][] arr = generateInitialCostArray(networks, links, networkElements);

		// update the initial cost array based of the minimum weight
		updateConsideringNetworkElement1(arr, networks);
		updateConsideringNetworkElement2(arr, networks);

		int minCost = Integer.MAX_VALUE;

		// iterate over possible values and get the minimum out of it
		for (int i = 0; i < possibleStartNetworkElements.size(); i++) {
			for (int j = 0; j < possibleEndNetworkElements.size(); j++) {

				int startNetworkElementIndex = Integer.parseInt(
						possibleStartNetworkElements.get(i).substring(possibleStartNetworkElements.get(i).length() - 1,
								possibleStartNetworkElements.get(i).length()));
				int endNetworkElementIndex = Integer.parseInt(
						possibleEndNetworkElements.get(j).substring(possibleStartNetworkElements.get(j).length() - 1,
								possibleStartNetworkElements.get(j).length()));

				minCost = Math.min(arr[startNetworkElementIndex][endNetworkElementIndex], minCost);
			}
		}
		if (minCost == Integer.MAX_VALUE) {
			System.out.println("Communication not possible!!");
			return;
		}
		System.out.println("Min Cost ::" + minCost);

	}

	// get all persons
	private static List<Person> getPersons() throws IOException, FileNotFoundException {
		List<Person> persons = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/persons.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Person person = new Person(values[0], values[1]);
				persons.add(person);
			}
		}
		return persons;
	}

	// get all networkelements
	private static List<NetworkElement> getNetworkElements() throws IOException, FileNotFoundException {
		List<NetworkElement> networkElements = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/network-element.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				NetworkElement link = new NetworkElement(values[0], values[1], values[2]);
				networkElements.add(link);
			}
		}
		return networkElements;
	}

	// get all networks
	private static List<Network> getNetworks() throws IOException, FileNotFoundException {
		List<Network> networks = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/networks.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Network network = new Network(values[0], values[1], values[2]);
				networks.add(network);
			}
		}
		return networks;
	}

	// get all links
	private static List<Link> getLinks() throws IOException, FileNotFoundException {
		List<Link> links = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/links.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				Link link = new Link(values[0], values[1]);
				links.add(link);
			}
		}
		return links;
	}

	private static boolean isValidSourceAndDestination(String source, String destination, List<Person> persons) {
		// TODO Auto-generated method stub
		Optional<Person> personSourceOptional = persons.stream()
				.filter(person -> person.getPersonName().equalsIgnoreCase(source)).findFirst();
		Optional<Person> personDestinationOptional = persons.stream()
				.filter(person -> person.getPersonName().equalsIgnoreCase(source)).findFirst();

		return personSourceOptional.isPresent() && personDestinationOptional.isPresent();

	}

	// pass the neighbour network to the considered network
	public static void updateConsideringNetworkElement1(int[][] arr, List<Network> networks) {
		networks.stream().forEach(network -> {
			String networkElementConsidered = network.getNetworkElement1();
			String networkElementNeighbour = network.getNetworkElement2();
			int consideredIndex = Integer.parseInt(networkElementConsidered
					.substring(networkElementConsidered.length() - 1, networkElementConsidered.length()));
			int neighbourIndex = Integer.parseInt(networkElementNeighbour
					.substring(networkElementNeighbour.length() - 1, networkElementNeighbour.length()));

			updateByPassing(arr, consideredIndex, neighbourIndex, arr[consideredIndex][neighbourIndex]);

		});
	}

	// updated the considered network element by comparing with neighbor
	private static void updateByPassing(int[][] arr, int consideredIndex, int neighbourIndex, int weight) {
		// TODO Auto-generated method stub
		int[] considered = arr[consideredIndex];
		int[] neighbour = arr[neighbourIndex];

		for (int i = 1; i < considered.length; i++) {
			if (i == consideredIndex) {
				considered[i] = 0;
			} else if (considered[i] == Integer.MAX_VALUE && neighbour[i] == Integer.MAX_VALUE) {
				considered[i] = Integer.MAX_VALUE;
			} else if (neighbour[i] == Integer.MAX_VALUE) {
				considered[i] = considered[i];
			} else {
				considered[i] = Math.min(considered[i], weight + neighbour[i]);
			}
		}

		arr[consideredIndex] = considered;
		arr[neighbourIndex] = neighbour;

	}

	public static void updateConsideringNetworkElement2(int[][] arr, List<Network> networks) {
		networks.stream().forEach(network -> {
			String networkElementConsidered = network.getNetworkElement2();
			String networkElementNeighbour = network.getNetworkElement1();
			int consideredIndex = Integer.parseInt(networkElementConsidered
					.substring(networkElementConsidered.length() - 1, networkElementConsidered.length()));
			int neighbourIndex = Integer.parseInt(networkElementNeighbour
					.substring(networkElementNeighbour.length() - 1, networkElementNeighbour.length()));

			updateByPassing(arr, consideredIndex, neighbourIndex, arr[consideredIndex][neighbourIndex]);

		});
	}

	// update the weight from row to col for every networkelement
	public static int[][] generateInitialCostArray(List<Network> networks, List<Link> links,
			List<NetworkElement> networkElements) {
		int numberOfNetworkElements = networkElements.size();

		int[][] arr = new int[numberOfNetworkElements + 1][numberOfNetworkElements + 1];

		for (int row = 0; row < numberOfNetworkElements + 1; row++) {
			for (int col = 0; col < numberOfNetworkElements + 1; col++) {
				if (col == 0 || row == 0) {
					arr[row][col] = 0;
				}
				if (col == row) {
					arr[row][col] = 0;
				} else if (row != 0 && col != 0) {
					String source = "NE" + row;
					String dest = "NE" + col;
					String link1 = "Link" + row + col;
					String link2 = "Link" + col + row;
					if (checkIfLinkIsPresentOrNot(source, dest, networks)) {
						arr[row][col] = calculatePrice(link1, link2, source, dest, links, networkElements);
					} else {
						arr[row][col] = Integer.MAX_VALUE;
					}
				}
			}
		}
		return arr;
	}

	private static int calculatePrice(String link1, String link2, String source, String dest, List<Link> links,
			List<NetworkElement> networkElements) {
		// TODO Auto-generated method stub
		Optional<Link> linkOptinal = links.stream()
				.filter(l -> l.getLink().equalsIgnoreCase(link1) || l.getLink().equalsIgnoreCase(link2)).findFirst();
		int price = 0;
		if (linkOptinal.isPresent())
			price = Integer.parseInt(linkOptinal.get().getPrice());

		int processingTimeSource = getProcessingTime(networkElements, source);
		int processingTimeDest = getProcessingTime(networkElements, dest);

		return (5 * (processingTimeDest + processingTimeSource) + 2 * (price));
	}

	private static int getProcessingTime(List<NetworkElement> networkElements, String source) {
		// TODO Auto-generated method stub
		return Integer.parseInt(
				networkElements.stream().filter(networkElement -> networkElement.getName().equalsIgnoreCase(source))
						.findFirst().get().getProcessingTime());

	}

	private static boolean checkIfLinkIsPresentOrNot(String source, String dest, List<Network> networks) {
		// TODO Auto-generated method stub
		return networks.stream().anyMatch((network) -> {
			if ((network.getNetworkElement1().equalsIgnoreCase(source)
					&& network.getNetworkElement2().equalsIgnoreCase(dest))
					|| (network.getNetworkElement1().equalsIgnoreCase(dest)
							&& network.getNetworkElement2().equalsIgnoreCase(source))) {
				return true;
			} else {
				return false;
			}
		});
	}

}
