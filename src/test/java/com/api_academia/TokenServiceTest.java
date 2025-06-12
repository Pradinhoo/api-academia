package com.api_academia;


import com.api_academia.model.TipoUsuario;
import com.api_academia.model.Usuario;
import com.api_academia.service.TokenService;
import com.api_academia.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private final String secretValido = "secretValido";
    private final String secretInvalido = "";

    @Test
    void deveRetornarTokenGeradoComSucesso() {
        TokenService service = new TokenServiceImpl(secretValido);
        Usuario usuario = new Usuario("UsuarioTeste","senhaTeste" , TipoUsuario.ADMIN);

        String token = service.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoErroAoCriarToken() {
        TokenService service = new TokenServiceImpl(secretInvalido);
        Usuario usuario = new Usuario("UsuarioTeste","senhaTeste" , TipoUsuario.ADMIN);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.gerarToken(usuario));
        assertEquals("Erro ao gerar token JWT", ex.getMessage());
    }

    @Test
    void deveRetornarSubjectComSucesso() {
        TokenService service = new TokenServiceImpl(secretValido);
        Usuario usuario = new Usuario("UsuarioTeste","senhaTeste" , TipoUsuario.ADMIN);

        String token = service.gerarToken(usuario);
        String subject = service.getSubject(token);

        assertEquals(usuario.getLogin(), subject);
    }

    @Test
    void deveLancarExcecaoQuandoTokenInvalido() {
        TokenService service = new TokenServiceImpl(secretValido);
        String tokenInvalido = "tokenInvalido";

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getSubject(tokenInvalido));
        assertEquals("Token JWT inválido ou expirado", ex.getMessage());
    }
}
