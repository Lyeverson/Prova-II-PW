package com.ufrn.PW.service;

import org.springframework.stereotype.Service;

import com.ufrn.PW.repository.CamisaRepository;

import jakarta.transaction.Transactional;

import com.ufrn.PW.domain.Camisa;

import java.util.List;
import java.util.Optional;

@Service
public class CamisaService{

private final CamisaRepository repository;

public CamisaService(CamisaRepository repository){
this.repository = repository;
}

 public Optional<Camisa> findById(Long id){
        return repository.findById(id);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    public Camisa update(Camisa c){
       return repository.saveAndFlush(c);
    }

    public Camisa create(Camisa c){
        return repository.save(c);
    }

    public List<Camisa> findAll(){
        return repository.findAll();
    }

}
