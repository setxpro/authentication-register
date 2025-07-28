package br.com.makeconsultores.oauth_register.infra.persistences.system;

import br.com.makeconsultores.oauth_register.infra.persistences.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_system_access")
public class SystemAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private Boolean active;

    @ManyToMany(mappedBy = "systems")
    private List<User> users;

    public SystemAccess(Long id, String name, String url, Boolean active, List<User> users) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.active = active;
        this.users = users;
    }

    public SystemAccess() {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
