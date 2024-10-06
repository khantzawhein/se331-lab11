package se331.lab.service;

import se331.lab.entity.Participant;
import se331.lab.entity.ParticipantDTO;

import java.util.List;

public interface ParticipantService {
    List<Participant> getAllParticipant();
}
