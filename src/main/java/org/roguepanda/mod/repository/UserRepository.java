package org.roguepanda.mod.repository;

import org.roguepanda.mod.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
	public User findByName(String name);
}
