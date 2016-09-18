package com.park.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.park.model.FeeCriterion;

@Repository
public interface FeeCriterionDAO {

	public List<FeeCriterion> get();

	public int insert(FeeCriterion criterion);

	public int modify(FeeCriterion criterion);

	public int delete(int id);

	public FeeCriterion getById(Integer id);

}
