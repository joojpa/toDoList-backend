package br.com.todolist.toDoList.entities;

public class UserEntity {

    private Long id;
    private String name;
    private String email;
    private String password;

    public UserEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nome inválido");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
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
