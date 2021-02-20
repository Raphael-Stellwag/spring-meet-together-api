package de.raphael.stellwag.spring.meettogether.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "person")
public class UserEntity {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;
    private String password;
    private boolean registered;
}
