package com.proxyseller.testtask.data

import org.springframework.data.repository.CrudRepository

interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findAllByPublicationId(String id)
}