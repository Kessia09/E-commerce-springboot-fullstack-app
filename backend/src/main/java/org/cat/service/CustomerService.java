package org.cat.irere.service;

import org.cat.irere.dto.CustomerDTO;
import org.cat.irere.dto.LoginDTO;
import org.cat.irere.dto.RegisterDTO;

import java.util.Optional;

public interface CustomerService {
    CustomerDTO register(RegisterDTO registerDTO);

    CustomerDTO login(LoginDTO loginDTO);

    Optional<CustomerDTO> getCustomerById(Long id);

    Optional<CustomerDTO> getCustomerByEmail(String email);
}