package com.proxyseller.testtask.data

import groovy.transform.TupleConstructor
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
@TupleConstructor(excludes = "id")
class Subscription {

    @Id
    String id
    SwitterUser user
    Set<SwitterUser> subscriptions
}
