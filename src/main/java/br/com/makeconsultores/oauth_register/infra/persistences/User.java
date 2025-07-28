package br.com.makeconsultores.oauth_register.infra.persistences;

import br.com.makeconsultores.oauth_register.infra.persistences.dtos.UserDTO;
import br.com.makeconsultores.oauth_register.infra.persistences.system.SystemAccess;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "tb_user")
public class User {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;

    @OneToOne
    @JoinColumn(name = "access_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Access access;

    @ManyToMany
    @JoinTable(
            name = "tb_user_system_access",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "system_access_id")
    )
    private List<SystemAccess> systems;

    public User(Long id, String name, String email, String phone, Access access, List<SystemAccess> systems) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.access = access;
        this.systems = systems;
    }

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.email = userDTO.email();
        this.phone = userDTO.phone();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public List<SystemAccess> getSystems() {
        return systems;
    }

    public void setSystems(List<SystemAccess> systems) {
        this.systems = systems;
    }
}
