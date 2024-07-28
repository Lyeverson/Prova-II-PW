package com.ufrn.PW.domain;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Camisa{

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @Size(min = 3, max = 30, message = "erro no cadastro do nome da camisa")
 @NotBlank(message = "o nome não pode conter caracteres em branco")
 @NotEmpty(message = "o nome não pode ser vazio")
 private String nome;


 private Float preco;
 
 private String descricao;
 
 @Size(min = 3, max = 30, message = "erro no cadastro do material da camisa")
 @NotBlank(message = "o nome do material da camisa não pode conter caracteres em branco")
 @NotEmpty(message = "o nome do material da camisa não pode ser vazio")
 private String material;
 
 private Date isDeleted;
 
 
 private String imageUri;

}
