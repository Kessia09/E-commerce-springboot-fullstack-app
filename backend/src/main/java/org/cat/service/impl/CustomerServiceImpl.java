package org.cat.irere.service.impl;

import org.cat.irere.dto.CustomerDTO;
import org.cat.irere.dto.LoginDTO;
import org.cat.irere.dto.RegisterDTO;
import org.cat.irere.model.Customer;
import org.cat.irere.repository.CustomerRepository;
import org.cat.irere.service.CustomerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public CustomerServiceImpl(CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public CustomerDTO register(RegisterDTO registerDTO) {
        if (customerRepository.existsByEmail(registerDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        Customer customer = new Customer(
                registerDTO.getFirstname(),
                registerDTO.getPhone(),
                registerDTO.getEmail(),
                passwordEncoder.encode(registerDTO.getPassword()));

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    public CustomerDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                Optional<Customer> customer = customerRepository.findByEmail(loginDTO.getEmail());
                return customer.map(this::convertToDTO)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
            } else {
                throw new IllegalArgumentException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<CustomerDTO> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstname(),
                customer.getPhone(),
                customer.getEmail());
    }
}