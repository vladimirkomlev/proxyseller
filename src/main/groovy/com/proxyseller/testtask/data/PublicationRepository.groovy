package com.proxyseller.testtask.data

import org.springframework.data.repository.CrudRepository

interface PublicationRepository extends CrudRepository<Publication, String> {
    List<Publication> findAllByUser(SwitterUser user)
}