package com.remiNorte.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.remiNorte.app.models.entity.PasswordResetToken;

public interface IPasswordResetTokenDao extends CrudRepository<PasswordResetToken, Long>{

}
