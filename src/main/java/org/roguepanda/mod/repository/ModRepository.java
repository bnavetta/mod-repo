package org.roguepanda.mod.repository;

import java.util.List;

import org.roguepanda.mod.domain.Mod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModRepository extends JpaRepository<Mod, Long>
{
	public List<Mod> findByName(String name);
}
