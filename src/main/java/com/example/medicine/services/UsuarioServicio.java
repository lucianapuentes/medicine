package com.example.medicine.services;



import com.example.medicine.errors.ErrorServicio;
import com.example.medicine.model.Usuario;
import com.example.medicine.repositories.UsuarioRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioServicio {
  @Autowired
  private UsuarioRepositorio usuarioRepositorio;

  @Autowired
  private UtilServicio utilServicio;

  // Busqueda

  @Transactional
  public List<Usuario> listarUsuario() throws ErrorServicio {
    try {
      return usuarioRepositorio.findAll();
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public List<Usuario> listarUsuarioActivo() throws ErrorServicio {
    try {
      return usuarioRepositorio.findAllActives();
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Usuario buscarUsuario(String id) throws ErrorServicio {
    try {
      Optional<Usuario> opt = usuarioRepositorio.findById(id);
      if (opt.isPresent()) {
        return opt.get();
      } else {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  @Transactional
  public Usuario buscarUsuarioPorNombre(String nombreUsuario) throws ErrorServicio {
    Optional<Usuario> opt = usuarioRepositorio.findByUsername(nombreUsuario);
    if (opt.isEmpty()) {
      return null; // devolvemos null en vez de tirar excepción
    }
    return opt.get();
  }

  // Escritura

  @Transactional
  public Usuario crearUsuario(String nombreUsuario, String clave) throws ErrorServicio {
    System.out.println("creando usuario");
    validar(nombreUsuario, clave);
    try{
      if (buscarUsuarioPorNombre(nombreUsuario) != null) {
        throw new ErrorServicio("El nombre de usuario ya existe");
      }
      Usuario usuario = new Usuario();
      usuario.setNombreUsuario(nombreUsuario);
      usuario.setClave(UtilServicio.encriptarClave(clave));
      usuario.setEliminado(false);
      usuarioRepositorio.save(usuario);
      System.out.println("creo usuario: " + usuario.getNombreUsuario());
      return usuario;
    }catch (Exception e) {
      e.printStackTrace(); // o usar logger.error("Error creando usuario", e);
      throw new ErrorServicio("Error del sistema: " + e.getMessage());
    }
  }

  @Transactional
  public void modificarUsuario(String id, String nombreUsuario, String clave) throws ErrorServicio {
    validar(nombreUsuario, clave);
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Usuario usuario = buscarUsuario(id);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      usuario.setNombreUsuario(nombreUsuario);
      usuario.setClave(UtilServicio.encriptarClave(clave));
      usuarioRepositorio.save(usuario);

    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Eliminacion

  @Transactional
  public void eliminarUsuario(String id) throws ErrorServicio {
    try{
      if (id == null) {
        throw new ErrorServicio("El id no puede ser nulo");
      }
      Usuario usuario = buscarUsuario(id);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      usuario.setEliminado(true);
      usuarioRepositorio.save(usuario);
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Login

  @Transactional
  public Usuario login(String nombreUsuario, String clave) throws ErrorServicio {
    try{
      if (nombreUsuario == null || nombreUsuario.isEmpty()) {
        throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacio");
      }
      if (clave == null || clave.isEmpty()) {
        throw new ErrorServicio("La clave no puede ser nula o estar vacia");
      }

      Usuario usuario = buscarUsuarioPorNombre(nombreUsuario);
      if (usuario == null) {
        throw new ErrorServicio("No se encontro el usuario solicitado");
      }
      if (!usuario.getClave().equals(UtilServicio.encriptarClave(clave))) {
        throw new ErrorServicio("La clave es incorrecta");
      }
      return usuario;
    }catch (Exception e) {
      throw new ErrorServicio("Error del sistema");
    }
  }

  // Validacion

  private void validar(String nombreUsuario, String clave) throws ErrorServicio {
    if(nombreUsuario == null || nombreUsuario.isEmpty()){
      throw new ErrorServicio("El nombre de usuario no puede ser nulo o estar vacío");
    }
    if(clave == null || clave.isEmpty()){
      throw new ErrorServicio("La clave no puede ser nula o estar vacía");
    }
    if (clave.length() < 6 || clave.length() > 12) {
      throw new ErrorServicio("La clave debe tener entre 6 y 12 caracteres");
    }
    
  }

  public boolean esAdmin(String id){
    Optional<Usuario> opt = usuarioRepositorio.findById(id);
    if(opt.isPresent()){
      Usuario u = opt.get();
      if(u.getId().equals("1")){
        return true;
      }else {
        return false;
      }
    }
    return false;
  }
}

