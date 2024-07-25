package co.com.jcgfdev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.com.jcgfdev.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
