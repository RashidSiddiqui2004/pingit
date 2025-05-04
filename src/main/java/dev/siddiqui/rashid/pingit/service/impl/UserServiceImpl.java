package dev.siddiqui.rashid.pingit.service.impl;

import dev.siddiqui.rashid.pingit.entity.User;
import dev.siddiqui.rashid.pingit.entity.UserDTO;
import dev.siddiqui.rashid.pingit.repository.UserRepository;
import dev.siddiqui.rashid.pingit.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDTO create(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User saved = repo.save(user);
        return convertToDTO(saved);
    }

    @Override
    public List<UserDTO> findAll() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return repo.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with id " + id));
    }

    @Override
    public UserDTO findByCollegeMail(String collegeMail) {
        return repo.findByCollegeMail(collegeMail).map(this::convertToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with college mail " + collegeMail));
    }

    @Override
    public UserDTO findByRollNumber(String rollNumber) {
        return repo.findByRollNo(rollNumber).map(this::convertToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with roll Number " + rollNumber));
    }

    @Override
    public UserDTO update(Long id, UserDTO updatedDTO) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found with id " + id));

        existing.setUsername(updatedDTO.username());
        existing.setPassword(updatedDTO.password());
        existing.setCollegeMail(updatedDTO.collegeMail());
        existing.setSubscribingMail(updatedDTO.subscribingMail());
        existing.setRollNumber(updatedDTO.rollNumber());
        existing.setBranch(updatedDTO.branch());

        User saved = repo.save(existing);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found with id " + id);
        }
        repo.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getCollegeMail(),
                user.getSubscribingMail(),
                user.getRollNumber(),
                user.getBranch());
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setCollegeMail(dto.collegeMail());
        user.setSubscribingMail(dto.subscribingMail());
        user.setRollNumber(dto.rollNumber());
        user.setBranch(dto.branch());
        return user;
    }
}
