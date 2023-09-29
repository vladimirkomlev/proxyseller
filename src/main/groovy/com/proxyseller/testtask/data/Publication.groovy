package com.proxyseller.testtask.data

import groovy.transform.TupleConstructor
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id

@Document
@TupleConstructor(excludes = "id")
class Publication {

    @Id
    String id
    String text
    SwitterUser user
}
