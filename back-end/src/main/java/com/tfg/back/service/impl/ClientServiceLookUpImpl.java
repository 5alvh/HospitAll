package com.tfg.back.service.impl;

import com.tfg.back.enums.SearchType;
import com.tfg.back.exceptions.user.UserNotFoundException;
import com.tfg.back.model.Client;
import com.tfg.back.repository.ClientRepository;
import com.tfg.back.service.ClientServiceLookUp;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientServiceLookUpImpl implements ClientServiceLookUp {

    private final ClientRepository clientRepository;

    public ClientServiceLookUpImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findClientById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id, SearchType.ID));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email, SearchType.EMAIL));
    }
}
