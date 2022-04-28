package com.example.rest;

import com.example.rest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Communication {

    @Autowired
    private RestTemplate restTemplate;

    private List<String> cookies;

    private final String URL = "http://94.198.50.185:7081/api/users";


    @PostConstruct
    private void app(){

        ResponseEntity<List<User>> users = getAllUser();
        System.out.println(users.getBody());

        User addUser = new User(3L, "James", "Brown", (byte) 29);
        ResponseEntity<String> responseEntityAddUser = addUser(addUser);
        System.out.println(responseEntityAddUser.getBody());

        addUser.setName("Thomas");
        addUser.setLastName("Shelby");
        ResponseEntity<String> responseEntityUpdateUser = updateUser(addUser);
        System.out.println(responseEntityUpdateUser.getBody());

        ResponseEntity<String> responseEntityDeleteUser = deleteUser(3L);
        System.out.println(responseEntityDeleteUser.getBody());
    }


    public ResponseEntity<List<User>> getAllUser(){

        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {});
        cookies = responseEntity.getHeaders().get("Set-Cookie").stream().peek(x-> System.out.println(x)).collect(Collectors.toList());
        return responseEntity;
    }

    public ResponseEntity<String> addUser(User user){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.postForEntity(URL, httpEntity, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> updateUser(User user){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, httpEntity, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> deleteUser(Long id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
        return responseEntity;
    }
}
