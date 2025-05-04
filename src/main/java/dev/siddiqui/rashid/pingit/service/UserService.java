package dev.siddiqui.rashid.pingit.service;

import dev.siddiqui.rashid.pingit.entity.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO create(UserDTO user);

    List<UserDTO> findAll();

    UserDTO findById(Long id);

    UserDTO findByCollegeMail(String mail);

    UserDTO findByRollNumber(String rollNumber);

    UserDTO update(Long id, UserDTO user);

    void delete(Long id);
}
