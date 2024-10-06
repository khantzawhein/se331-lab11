package se331.lab.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se331.lab.entity.Event;
import se331.lab.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("manual")
public class EventDaoImpl implements EventDao {
    private final EventRepository eventRepository;
    List<Event> eventList;

    public EventDaoImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    public void init() {
        eventList = new ArrayList<>();

        eventList.add(Event.builder()
                .id(1L)
                .category("Concert")
                .title("Rock on the Hill")
                .description("Rock concert")
                .location("Moon Hill")
                .date("2021-08-30")
                .time("15:00")
                .petAllowed(false)
                .build());

        eventList.add(Event.builder()
                .id(2L)
                .category("Sport")
                .title("Swim with the Shark")
                .description("Swimming with the shark in the ocean")
                .location("Ocean")
                .date("2021-08-31")
                .time("09:00")
                .petAllowed(false)
                .build());

        eventList.add(Event.builder()
                .id(3L)
                .category("Sport")
                .title("Run in the Jungle")
                .description("Running in the jungle")
                .location("Jungle")
                .date("2021-09-01")
                .time("06:00")
                .petAllowed(false)
                .build());

        eventList.add(Event.builder()
                .id(4L)
                .category("Sport")
                .title("Bike in the Mountain")
                .description("Biking in the mountain")
                .location("Mountain")
                .date("2021-09-02")
                .time("07:00")
                .petAllowed(false)
                .build());

        eventList.add(Event.builder()
                .id(5L)
                .category("Concert")
                .title("Pop on the Beach")
                .description("Pop concert")
                .location("Sun Beach")
                .date("2021-09-03")
                .time("16:00")
                .petAllowed(false)
                .build());

        eventList.add(Event.builder()
                .id(6L)
                .category("Sport")
                .title("Soccer in the Field")
                .description("Soccer match")
                .location("Field")
                .date("2021-09-04")
                .time("17:00")
                .petAllowed(false)
                .build());
    }

    @Override
    public Integer getEventSize() {
        return eventList.size();
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? eventList.size() : pageSize;
        page = page == null ? 1 : page;

        int firstIndex = (page - 1) * pageSize;

        return new PageImpl<Event>(eventList.subList(firstIndex, firstIndex + pageSize), PageRequest.of(page - 1, pageSize), eventList.size());
    }

    @Override
    public Event getEvent(Long id) {
        return eventList.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Event save(Event event) {
        event.setId(eventList.get(eventList.size() - 1).getId() + 1);
        eventList.add(event);
        return event;
    }

    @Override
    public Page<Event> getEvents(String title, Pageable page) {
        return eventRepository.findByTitle(title, page);
    }
}