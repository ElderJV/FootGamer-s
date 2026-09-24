package org.example.footgamers.service.implementacion;

import lombok.RequiredArgsConstructor;
import org.example.footgamers.dto.request.UsuarioRequestDto;
import org.example.footgamers.dto.response.UsuarioResponseDto;
import org.example.footgamers.entities.Usuario;
import org.example.footgamers.exception.ApiException;
import org.example.footgamers.exception.MensajeException;
import org.example.footgamers.repository.UsuarioRepository;
import org.example.footgamers.service.reglas.IUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements IUsuario {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto request) {
        validarUsuarioUnico(null, request);
        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setContrasena(request.contrasena());
        usuario.setRol(request.rol());
        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public Page<UsuarioResponseDto> obtenerTodos(Long pagina, Long tamano) {
        return usuarioRepository.findAll(PageRequest.of(pagina.intValue(), tamano.intValue()))
                .map(this::toResponse);
    }

    @Override
    public UsuarioResponseDto obtenerPorId(Long id) {
        return toResponse(buscarUsuario(id));
    }

    @Override
    public UsuarioResponseDto actualizar(Long id, UsuarioRequestDto request) {
        Usuario usuario = buscarUsuario(id);
        validarUsuarioUnico(usuario, request);
        usuario.setUsername(request.username());
        usuario.setEmail(request.email());
        usuario.setContrasena(request.contrasena());
        usuario.setRol(request.rol());
        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.delete(usuario);
    }

    private void validarUsuarioUnico(Usuario usuario, UsuarioRequestDto request) {
        boolean esNuevo = usuario == null;
        if ((esNuevo || !usuario.getUsername().equals(request.username()))
                && usuarioRepository.existsByUsername(request.username())) {
            throw ApiException.valorYaEnUso(
                    String.format(MensajeException.VALOR_YA_EN_USO, "usuario", "username", request.username()));
        }
        if ((esNuevo || !usuario.getEmail().equals(request.email()))
                && usuarioRepository.existsByEmail(request.email())) {
            throw ApiException.valorYaEnUso(
                    String.format(MensajeException.VALOR_YA_EN_USO, "usuario", "email", request.email()));
        }
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> ApiException.noEncontrado(
                        String.format(MensajeException.VALOR_NO_ENCONTRADO, "usuario", id)));
    }

    private UsuarioResponseDto toResponse(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}