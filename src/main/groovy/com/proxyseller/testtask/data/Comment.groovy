package com.proxyseller.testtask.data

import groovy.transform.TupleConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@TupleConstructor(excludes = "id")
class Comment {

    @Id
    String id
    String text
    Publication publication
    SwitterUser user
}
