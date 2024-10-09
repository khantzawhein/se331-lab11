package se331.lab.config;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import se331.lab.entity.Event;
import se331.lab.entity.Organizer;
import se331.lab.entity.Participant;
import se331.lab.repository.EventRepository;
import se331.lab.repository.OrganizerRepository;
import se331.lab.repository.ParticipantRepository;
import se331.lab.user.Role;
import se331.lab.user.User;
import se331.lab.user.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    final EventRepository eventRepository;
    final OrganizerRepository organizerRepository;
    final ParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Organizer org1, org2, org3;
        Participant p1, p2, p3, p4, p5;

        org1 = organizerRepository.save(Organizer.builder().name("CAMT").build());
        org2 = organizerRepository.save(Organizer.builder().name("CMU").build());
        org3 = organizerRepository.save(Organizer.builder().name("Chiang Mai").build());

        p1 = participantRepository.save(Participant.builder().name("Khant").telNo("0800000001").build());
        p2 = participantRepository.save(Participant.builder().name("Joy").telNo("0800000002").build());
        p3 = participantRepository.save(Participant.builder().name("Win").telNo("0800000003").build());
        p4 = participantRepository.save(Participant.builder().name("Cherprang").telNo("0800000004").build());
        p5 = participantRepository.save(Participant.builder().name("Music").telNo("0800000005").build());

        Event tempEvent;


        tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
                .title("Midterm Exam")
                .description("A time for taking the exam")
                .location("CAMT Building")
                .date("3rd Sept")
                .time("3.00-4.00 pm")
                .petAllowed(false)
                .build());

        tempEvent.setOrganizer(org1);
        p1.getEventHistory().add(tempEvent);
        p2.getEventHistory().add(tempEvent);
        p3.getEventHistory().add(tempEvent);
        org1.getOwnEvents().add(tempEvent);


        tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
                .title("Commencement Day")
                .description("A time for celebration")
                .location("CMU Convention Hall")
                .date("21st Jan")
                .time("8.00-4.00 pm")
                .petAllowed(false)
                .build());

        tempEvent.setOrganizer(org2);
        p1.getEventHistory().add(tempEvent);
        p2.getEventHistory().add(tempEvent);
        p3.getEventHistory().add(tempEvent);
        p4.getEventHistory().add(tempEvent);
        org2.getOwnEvents().add(tempEvent);

        tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Loy Krathong")
                .description("A time for Krathong")
                .location("Ping River")
                .date("21st Nov")
                .time("8.00-10.00 pm.")
                .petAllowed(false)
                .build());

        tempEvent.setOrganizer(org3);
        p1.getEventHistory().add(tempEvent);
        p2.getEventHistory().add(tempEvent);
        p3.getEventHistory().add(tempEvent);
        org3.getOwnEvents().add(tempEvent);

        tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Songkran")
                .description("Let's Play Water")
                .location("Chiang Mai Moat")
                .date("13th April")
                .time("10.00-6.00 pm.")
                .petAllowed(true)
                .build());
        tempEvent.setOrganizer(org3);
        p4.getEventHistory().add(tempEvent);
        p5.getEventHistory().add(tempEvent);
        org3.getOwnEvents().add(tempEvent);
        addUser();

        org1.setUser(user1);

        user1.setOrganizer(org1);
        org2.setUser(user2);
        user2.setOrganizer(org2);
        org3.setUser(user3);
        user3.setOrganizer(org3);
    }

    User user1, user2, user3;

    private void addUser() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user1 = User.builder()
                .username("admin")
                .password(encoder.encode("admin"))
                .firstname("admin")
                .lastname("admin")
                .email("admin@admin.com")
                .enabled(true)
                .build();

        user2 = User.builder()
                .username("user")
                .password(encoder.encode("user"))
                .firstname("user")
                .lastname("user")
                .email("enabled@user.com")
                .enabled(true)
                .build();

        user3 = User.builder()
                .username("disabledUser")
                .password(encoder.encode("disabledUser"))
                .firstname("disabledUser")
                .lastname("disabledUser")
                .email("disableUser@user.com")
                .build();

        user1.getRoles().add(Role.ROLE_USER);
        user1.getRoles().add(Role.ROLE_ADMIN);

        user2.getRoles().add(Role.ROLE_USER);
        user3.getRoles().add(Role.ROLE_USER);


        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

    }
}
