package com.proxyseller.testtask.web

import com.proxyseller.testtask.data.Comment
import com.proxyseller.testtask.data.CommentRepository
import com.proxyseller.testtask.data.Publication
import com.proxyseller.testtask.data.PublicationRepository
import com.proxyseller.testtask.data.SwitterUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/comment")
class CommentController {

    @Autowired
    CommentRepository commentRepository

    @Autowired
    PublicationRepository publicationRepository

    @ModelAttribute
    Comment setupCommentForForm() {
        return new Comment()
    }

    @GetMapping
    String comment(@RequestParam String pubId, Model model) {
        model.addAttribute("pubId", pubId)
        return "comment"
    }

    @PostMapping
    String processComment(@RequestParam String pubId, Comment comment, @AuthenticationPrincipal SwitterUser user) {
        comment.setUser(user)
        Optional<Publication> publicationOpt = publicationRepository.findById(pubId)
        if (publicationOpt.isPresent()) {
            Publication publication = publicationOpt.get()
            comment.setPublication(publication)
            commentRepository.save(comment)
        }
        return "redirect:/publication/" + pubId
    }
}
