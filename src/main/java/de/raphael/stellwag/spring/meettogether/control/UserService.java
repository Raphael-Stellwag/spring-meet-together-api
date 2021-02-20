package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.entity.dao.UserRepository;
import de.raphael.stellwag.spring.meettogether.entity.model.UserEntity;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final EntityToDto entityToDto;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, EntityToDto entityToDto, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.entityToDto = entityToDto;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createNotRegisteredUser(UserDto body) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(body.getName());
        userEntity.setRegistered(false);
        userEntity.setPassword(passwordEncoder.encode(body.getPassword()));

        UserEntity writtenEntity = userRepository.insert(userEntity);

        return entityToDto.getUserDto(writtenEntity);
    }

    public UserDto renameUser(String userId, String name) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_FOUND);
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setName(name);
        userEntity = userRepository.save(userEntity);
        return entityToDto.getUserDto(userEntity);
    }

    public Iterable<UserEntity> getUserEntities(List<String> ids) {
        return userRepository.findAllById(ids);
    }

    public UserDto registerUser(UserDto user) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(user.getId());
        if (optionalUserEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_FOUND);
        }
        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setName(user.getName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRegistered(true);
        userEntity = userRepository.save(userEntity);
        return entityToDto.getUserDto(userEntity);
    }

    public boolean doesEmailExist(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        return optionalUserEntity.isPresent();
    }

    public String getUserId(String email, String password) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.EMAIL_DOES_NOT_EXIST);
        }
        if (!passwordEncoder.matches(password, optionalUserEntity.get().getPassword())) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.PASSWORD_NOT_CORRECT);
        }
        return optionalUserEntity.get().getId();
    }

    public UserDto getUserDto(String id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_FOUND);
        }
        return entityToDto.getUserDto(optionalUserEntity.get());
    }

    public String getUserName(String userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new MeetTogetherException(MeetTogetherExceptionEnum.USER_NOT_FOUND);
        }
        return userEntity.get().getName();
    }
}
