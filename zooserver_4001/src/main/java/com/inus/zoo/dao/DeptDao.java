
package com.inus.zoo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.inus.zoo.domain.Dept;

@ Mapper
public interface DeptDao
{
    public boolean add(Dept dept);

    public Dept getById(int no);

    public List<Dept> getAll();
}
