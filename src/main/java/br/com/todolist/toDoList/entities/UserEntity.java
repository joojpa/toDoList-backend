package br.com.todolist.toDoList.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "tb_users")
@NoArgsConstructor
public class UserEntity {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    @Getter
    private String password;

    public void setName(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome inválido");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")){
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank() || password.length() < 6){
            throw new IllegalArgumentException("Senha inválida");
        }
        this.password = password;
    }
}
