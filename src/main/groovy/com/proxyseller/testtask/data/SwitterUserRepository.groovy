package com.proxyseller.testtask.data

import org.springframework.data.repository.CrudRepository

interface SwitterUserRepository extends CrudRepository<SwitterUser, String> {
    SwitterUser findByUsername(String username)
}