package de.raphael.stellwag.spring.meettogether.control;

import de.raphael.stellwag.generated.dto.UserDto;
import de.raphael.stellwag.spring.meettogether.entity.UserEntity;
import de.raphael.stellwag.spring.meettogether.entity.UserRepository;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherException;
import de.raphael.stellwag.spring.meettogether.error.MeetTogetherExceptionEnum;
import de.raphael.stellwag.spring.meettogether.helpers.DtoToEntity;
import de.raphael.stellwag.spring.meettogether.helpers.EntityToDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DtoToEntity dtoToEntity;
    private final EntityToDto entityToDto;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, DtoToEntity dtoToEntity, EntityToDto entityToDto, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dtoToEntity = dtoToEntity;
        this.entityToDto = entityToDto;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createNotRegisteredUser(UserDto body) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(body.getName());
        userEntity.setRegistered(false);
        userEntity.setPassword(passwordEncoder.encode(body.getPassword()));

        UserEntity writtenEntity = userRepository.save(userEntity);

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
}
