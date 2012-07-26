package org.roguepanda.mod.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.roguepanda.mod.domain.Mod;

public class ModRepositoryImpl implements ModRepositoryCustom
{
	@PersistenceContext
	private EntityManager entityManager;
	
	public List<Mod> findRecent(int number)
	{
		return entityManager.createQuery("SELECT m FROM Mod m ORDER BY m.created DESC", Mod.class)
				.setMaxResults(number)
				.getResultList();
	}

}
