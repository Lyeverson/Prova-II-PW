package com.ufrn.PW.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ufrn.PW.domain.Usuario;
import com.ufrn.PW.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Autowired
    public UserService(UsuarioRepository repository){
        this.repository = repository;
    }

    public Optional<Usuario> findById(Long id){
        return repository.findById(id);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Usuario create(Usuario user){
        return repository.save(user);
    } 

    public List<Usuario> findAll(){
        return repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }
}
