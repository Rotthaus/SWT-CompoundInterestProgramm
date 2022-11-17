package com.swt.project.authService.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swt.project.compoundService.entity.CompoundModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Users {

    @ApiParam( hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated
    @Column(name = "user_role")
    private UserRole userRole;

    @OneToMany(mappedBy = "idUser", cascade = CascadeType.REMOVE)
    private List<CompoundModel> entries;
}