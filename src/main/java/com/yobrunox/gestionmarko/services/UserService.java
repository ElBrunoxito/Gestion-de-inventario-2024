package com.yobrunox.gestionmarko.services;

import com.yobrunox.gestionmarko.dto.auth.LoginRequest;
import com.yobrunox.gestionmarko.dto.auth.RegisterRequest;
import com.yobrunox.gestionmarko.dto.auth.TokenResponse;
import com.yobrunox.gestionmarko.dto.business.CreateUpdateBusinessDTO;
import com.yobrunox.gestionmarko.dto.exception.BusinessException;
import com.yobrunox.gestionmarko.models.*;
import com.yobrunox.gestionmarko.repository.RoleRepository;
import com.yobrunox.gestionmarko.repository.UserRepository;
import com.yobrunox.gestionmarko.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;


    //Service
    private final BusinessService businessService;

    //private final JwtProvider jwtProvider;
    //private final AuthenticationManager authenticationManager;
    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        //GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+user.getAuthorities());
        UserEntity users = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() ->
                new BusinessException("M-400", HttpStatus.ALREADY_REPORTED,"Usuario no existente")

        );


        //UserDetails user = users;


        String token = jwtProvider.generateToken(users);
        /*Date expirationDate = jwtProvider.getExpiration(token);*/

/*        Set<String> roles = new HashSet<>();
        users.getRoles().forEach(role -> {
            roles.add(role.getRole().name());
        });*/
        //System.out.println(roles);

        return TokenResponse.builder()
                .access_token(token)
                .refresh_token(jwtProvider.refreshToken(users))
                //.Roles(roles)
                //.expirationDate(expirationDate)
                .expires_in(jwtProvider.getExpiration(token))
                .build();
    }
    @Transactional
    public TokenResponse register(RegisterRequest userDTO){
        //Set<ERole> roles = new HashSet<>();
        //roles.add(ERole.USER);
        //roles.add(ERole.ADMIN);

        //Business business = businessService.createBusiness(new CreateUpdateBusinessDTO());



        List<Role> gerRoles = roleRepository.findAll();
        Set<Role> roles;


        if(gerRoles.size() == 0){
            roles = Set.of(Role.builder()
                            .role(ERole.USER)
                            .build(),
                    Role.builder()
                            .role(ERole.ADMIN)
                            .build()
            );
        }else {
            roles = gerRoles.stream().collect(Collectors.toSet());
        }


        UserEntity user = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .isCreator(true)
                //.business()
                .roles(roles)
                .build();
        userRepository.save(user);
        //Business business = new Business();
        //return "Usuario registrado correctamente";
        String token = jwtProvider.generateToken(user);
        System.out.println("sdddsad");
        return TokenResponse.builder()
                .access_token(token)
                .refresh_token(jwtProvider.refreshToken(user))
                .expires_in(jwtProvider.getExpiration(token))
                .build();
    }


    public String registerForAdmin(RegisterRequest userDTO,String token){
        String username = jwtProvider.getUsernameFromToken(token);
        UserEntity admin = userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException("M-400", HttpStatus.BAD_REQUEST,"Usuario creador no exite, intentelo denuevo mas tarde")
        );


        Role userRole = roleRepository.findByRole(ERole.USER)
                .orElseGet(() -> {
                    // Si no existe, lo crea
                    Role newUserRole = Role.builder().role(ERole.USER).build();
                    return roleRepository.save(newUserRole);
                });


        UserEntity user = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .admin(admin)
                .isCreator(false)
                .business(admin.getBusiness())
                //.roles(Set.of(Role.builder().role(ERole.valueOf(ERole.USER.name())).build()))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);

        if(user != null){
            return "Usuario " + user.getUsername() + " registrado correctamente";
        }else{
            return "Error, pero este error no esta capturado, no hay excepciones";
        }
    }







}
