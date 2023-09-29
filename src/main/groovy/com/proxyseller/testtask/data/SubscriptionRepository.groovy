package com.proxyseller.testtask.data

import org.springframework.data.repository.CrudRepository

interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    Subscription findByUser(SwitterUser user)
}