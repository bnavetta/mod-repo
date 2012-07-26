package org.roguepanda.mod.repository;

import java.util.List;

import org.roguepanda.mod.domain.Mod;

public interface ModRepositoryCustom
{
	public List<Mod> findRecent(int number);
}
