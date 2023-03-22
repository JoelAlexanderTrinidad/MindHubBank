package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password
    ){

        String errors = "";


        if(firstName.isEmpty()){
            errors += "Missing first name";
        }
        if( lastName.isEmpty()){
            errors += "-Missing last name";
        }
        if(email.isEmpty()){
            errors += "-Missing email";
        }
        if(password.isEmpty()){
            errors += "-Missing password";
        }
        if(errors.equals("")){
            if(clientService.findByEmail(email) != null){
                return new ResponseEntity<>("Email already in use", HttpStatus.BAD_REQUEST);
            }

            Random random = new Random();
            int randomNumber = random.nextInt(999999);
            String randomNumberStr = String.format("%05d", randomNumber);

            Account account = new Account(LocalDateTime.now(), (double) 0, "VIN"+"-"+randomNumberStr, false, AccountType.CHECKING);
            Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
            client.addAccount(account);

            clientService.save(client);
            accountService.save(account);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        // clientRepository.findAll() devuelve una lista de obj de tipo client
        return clientService.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        ClientDTO clientDTO = new ClientDTO(clientService.findById(id));
        return clientDTO;
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication) {

        String email = authentication.getName();
        Client client = clientService.findByEmail(email);

        return new ClientDTO(client);
    }
}