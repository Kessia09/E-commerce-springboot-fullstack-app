package org.cat.service;

import org.cat.dto.CustomerDTO;
import org.cat.dto.LoginDTO;
import org.cat.dto.RegisterDTO;

import java.util.Optional;

public interface CustomerService {
    CustomerDTO register(RegisterDTO registerDTO);

    CustomerDTO login(LoginDTO loginDTO);

    Optional<CustomerDTO> getCustomerById(Long id);

    Optional<CustomerDTO> getCustomerByEmail(String email);
}
