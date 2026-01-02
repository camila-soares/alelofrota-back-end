package com.plataformaempregos.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plataformaempregos.domain.Usuario;
import com.plataformaempregos.dtos.LoginDTO;
import com.plataformaempregos.dtos.TokenDTO;
import com.plataformaempregos.services.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "Autenticação")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    @ApiOperation("Realizar login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        Optional<Usuario> usuarioOpt = authService.validarCredenciais(loginDTO.getEmail(), loginDTO.getSenha());
        
        if (usuarioOpt.isPresent()) {
            String token = authService.gerarToken(usuarioOpt.get());
            TokenDTO tokenDTO = TokenDTO.builder()
                    .token(token)
                    .tipo("Bearer")
                    .usuarioId(usuarioOpt.get().getId())
                    .tipoUsuario(usuarioOpt.get().getTipoUsuario().name())
                    .build();
            return ResponseEntity.ok(tokenDTO);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
