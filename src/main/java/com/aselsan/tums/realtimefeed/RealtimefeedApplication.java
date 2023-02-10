package com.aselsan.tums.realtimefeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@SpringBootApplication
public class RealtimefeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealtimefeedApplication.class, args);
	}

	@Bean
	ProtobufHttpMessageConverter protobufHttpMessageConverter() {
		return new ProtobufHttpMessageConverter();
	}

	private CustomerProtos.Customer customer(int id, String firstname, String lastname, Collection<String> emails) {

		Collection<CustomerProtos.Customer.EmailAddress> emailAddresses =
				emails.stream().map(e -> CustomerProtos.Customer.EmailAddress.newBuilder()
								.setType(CustomerProtos.Customer.EmailType.PROFESSIONAL)
								.setEmail(e).build())
						.collect(Collectors.toList());

		return CustomerProtos.Customer.newBuilder()
				.setFirstName(firstname)
				.setLastName(lastname)
				.setId(id)
				.addAllEmail(emailAddresses)
				.build();
	}

	private GtfsRealtime.FeedMessage tripUpdate(String id) {

		GtfsRealtime.FeedHeader feedHeader = GtfsRealtime.FeedHeader.newBuilder()
				.setGtfsRealtimeVersion("2.0")
				.setIncrementality(GtfsRealtime.FeedHeader.Incrementality.FULL_DATASET)
				.setTimestamp(1656230726)
				.build();

		GtfsRealtime.TripUpdate.StopTimeUpdate t = GtfsRealtime.TripUpdate.StopTimeUpdate.newBuilder()
				.setStopSequence(1)
				.setArrival(GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder().setDelay(-180).build())
				.build();

		Collection<GtfsRealtime.TripUpdate.StopTimeUpdate> allUpdates = asList(t);
		GtfsRealtime.TripUpdate u =	GtfsRealtime.TripUpdate.newBuilder()
				.setTrip(GtfsRealtime.TripDescriptor.newBuilder()
						.setTripId("1010_fbbcafb7")
						.setStartTime("06:50:00")
						.build())
				.addAllStopTimeUpdate(allUpdates)
				.build();


		GtfsRealtime.FeedEntity feedEntity = GtfsRealtime.FeedEntity.newBuilder()
				.setId("1")
				.setTripUpdate(u)
				.build();

		GtfsRealtime.FeedMessage feedMessage = GtfsRealtime.FeedMessage.newBuilder()
				.setHeader(feedHeader)
				.addAllEntity(asList(feedEntity))
				.build();

		return feedMessage;
	}

	@Bean
	CustomerRepository customerRepository() {

		Map<Integer, CustomerProtos.Customer> customers = new ConcurrentHashMap<>();
		// populate with some dummy data
		asList(
				customer(1, "Chris", "Richardson", asList("crichardson@email.com")),
				customer(2, "Josh", "Long", asList("jlong@email.com")),
				customer(3, "Matt", "Stine", asList("mstine@email.com")),
				customer(4, "Russ", "Miles", asList("rmiles@email.com"))
		).forEach(c -> customers.put(c.getId(), c));

		return new CustomerRepository() {

			@Override
			public CustomerProtos.Customer findById(int id) {
				return customers.get(id);
			}

			@Override
			public CustomerProtos.CustomerList findAll() {
				return CustomerProtos.CustomerList.newBuilder() //
						.addAllCustomer(customers.values()) //
						.build();
			}

			@Override
			public GtfsRealtime.FeedMessage getUpdate() {
				return tripUpdate("1");
			}
		};
	}
}
