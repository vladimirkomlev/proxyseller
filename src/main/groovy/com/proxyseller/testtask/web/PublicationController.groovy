package com.proxyseller.testtask.web

import com.proxyseller.testtask.data.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/publication")
class PublicationController {

    @Autowired
    PublicationRepository publicationRepository

    @Autowired
    SwitterUserRepository userRepository

    @Autowired
    SubscriptionRepository subscriptionRepository

    @Autowired
    CommentRepository commentRepository

    @ModelAttribute
    Publication setupForm() {
        return new Publication()
    }

    @GetMapping
    String publication() {
        return "publication"
    }

    @PostMapping
    String processPublication(Publication publication, @AuthenticationPrincipal SwitterUser user) {
        publication.setUser(user)
        publicationRepository.save(publication)
        return "redirect:/publication/" + publication.id
    }

    @GetMapping("/{id}")
    String publicationById(@PathVariable String id, Model model, @AuthenticationPrincipal SwitterUser user) {
        Optional<Publication> publicationOpt = publicationRepository.findById(id)
        if (publicationOpt.isPresent()) {
            Publication publication = publicationOpt.get()
            Subscription subscription = subscriptionRepository.findByUser(user)
            if (subscription != null) {
                SwitterUser subscribedUser = subscription.getSubscriptions().find { it.username == publication.getUser().username }
                model.addAttribute("subscribedUser", subscribedUser)
            }
            List<Comment> commentList = commentRepository.findAllByPublicationId(id)
            model.addAttribute("comments", commentList)
            model.addAttribute("currentPub", publication)
            model.addAttribute("currentUsername", user.username)
        }
        return "createdPub"
    }

    @PutMapping("/{id}")
    String updatePublication(@PathVariable String id, Model model, @AuthenticationPrincipal SwitterUser user) {
        Optional<Publication> publication = publicationRepository.findById(id)
        if (publication.isPresent()) {
            Publication foundPub = publication.get()
            if (foundPub.getUser().id == user.id) {
                model.addAttribute("currentPub", foundPub)
                return "publicationUpdate"
            } else {
                model.addAttribute("userError", "Only the owner can edit the publication")
                model.addAttribute("currentPub", foundPub)
                return "createdPub"
            }
        }
        return "publication"
    }

    @PatchMapping("/{id}")
    String processUpdatePublication(@PathVariable String id, Publication publicationText, @AuthenticationPrincipal SwitterUser user) {
        Optional<Publication> foundPub = publicationRepository.findById(id)
        if (foundPub.isPresent() && foundPub.get().getUser().id == user.id) {
            Publication publication = foundPub.get()
            publication.setText(publicationText.getText())
            publicationRepository.save(publication);
        }
        return "redirect:/publication/" + id
    }

    @DeleteMapping("/{id}")
    String deletePublication(@PathVariable String id, Model model, @AuthenticationPrincipal SwitterUser user) {
        Optional<Publication> publication = publicationRepository.findById(id)
        if (publication.isPresent()) {
            Publication foundPub = publication.get()
            if (foundPub.getUser().id == user.id) {
                List<Comment> comments = commentRepository.findAllByPublicationId(id)
                if (comments != null && comments.size() > 0) {
                    commentRepository.deleteAll(comments)
                }
                publicationRepository.deleteById(foundPub.id)
                return "redirect:/publication"
            } else {
                model.addAttribute("userError", "Only the owner can delete the publication")
                model.addAttribute("currentPub", foundPub)
                return "createdPub"
            }
        }
        return "publication"
    }

    @GetMapping("/user")
    String getPublicationsByAuthorizedUser(Model model, @AuthenticationPrincipal SwitterUser user) {
        List<Publication> listPub = publicationRepository.findAllByUser(user)
        model.addAttribute("pubs", listPub)
        return "publications"
    }

    @GetMapping("/user/{id}")
    String getPublicationsByUserId(@PathVariable String id, Model model) {
        Optional<SwitterUser> userOpt = userRepository.findById(id)
        if (userOpt.isPresent()) {
            SwitterUser user = userOpt.get()
            List<Publication> listPub = publicationRepository.findAllByUser(user)
            model.addAttribute("pubs", listPub)
            model.addAttribute("userName", user.username)
        }
        return "userPublications"
    }

    @GetMapping("/users/all")
    String getAllPublications(Model model) {
        Iterable<Publication> listPub = publicationRepository.findAll()
        model.addAttribute("pubs", listPub)
        return "allPublications"
    }
}
