package com.proxyseller.testtask.web

import com.proxyseller.testtask.data.Subscription
import com.proxyseller.testtask.data.SubscriptionRepository
import com.proxyseller.testtask.data.SwitterUser
import com.proxyseller.testtask.data.SwitterUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/subscription")
class SubscriptionController {

    @Autowired
    SubscriptionRepository subscriptionRepository

    @Autowired
    SwitterUserRepository userRepository

    @ModelAttribute("sUser")
    SwitterUser setupForm() {
        return new SwitterUser()
    }


    @GetMapping("/my")
    String getAllByUserId(Model model, @AuthenticationPrincipal SwitterUser user) {
        Subscription subscription = subscriptionRepository.findByUser(user)
        if (subscription != null) {
            model.addAttribute("subs", subscription.getSubscriptions())
        }
        return "subscriptions"
    }

    @PostMapping("/subscribe/{id}")
    String processSubscribe(@PathVariable String id, Model model, @AuthenticationPrincipal SwitterUser user) {
        if (user.id != id) {
            Optional<SwitterUser> subscribeUserOpt = userRepository.findById(id)
            if (subscribeUserOpt.isPresent()) {
                SwitterUser subscribeUser = subscribeUserOpt.get()
                Subscription subscription = subscriptionRepository.findByUser(user)
                if (subscription != null) {
                    subscription.getSubscriptions().add(subscribeUser)
                } else {
                    subscription = new Subscription()
                    subscription.setUser(user)
                    subscription.setSubscriptions(Set.of(subscribeUser))
                }
                subscriptionRepository.save(subscription)
                model.addAttribute("subs", subscription.getSubscriptions())
                return "subscriptions"
            }
        } else {
            model.addAttribute("userError", "Something went wrong with the subscription")
            return "page"
        }
    }

    @PostMapping("/unsubscribe/{id}")
    String processUnsubscribe(@PathVariable String id, Model model, @AuthenticationPrincipal SwitterUser user) {
        Subscription subscription = subscriptionRepository.findByUser(user)
        if (subscription != null) {
            subscription.getSubscriptions().removeIf { it.id == id }
            subscriptionRepository.save(subscription)
            model.addAttribute("subs", subscription.getSubscriptions())
        }
        return "subscriptions"
    }
}
