package com.example.restful_web_services.user;

import com.example.restful_web_services.jpa.PostRepository;
import com.example.restful_web_services.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserJpaResource {

    private UserRepository repository;
    private PostRepository postRepository;


    public UserJpaResource(UserRepository repository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.repository = repository;
    }

    @GetMapping("jpa/users")
    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    @GetMapping("jpa/users/{id}")
    public EntityModel<User> retrieveUsers(@PathVariable int id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id=" + id);
        }
        EntityModel<User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        entityModel.add(link.withRel("all-users"));
        return entityModel;
    }

    @PostMapping("jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = repository.save(user);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").
                buildAndExpand(savedUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("jpa/users/{id}/post")
    public ResponseEntity<Post> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) throw new UserNotFoundException("id=" + id);
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").
                buildAndExpand(savedPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }

    @GetMapping("jpa/users/{id}/posts")
    public List<Post> retrievePostForUser(@PathVariable int id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id=" + id);
        }
        return user.get().getPosts();
    }

//    @GetMapping("jpa/users/{id}/posts/{id}")
//    public Post retrievePostById(@PathVariable int id, @PathVariable int postId) {
//        Optional<User> user = repository.findById(id);
//        if (user.isEmpty()) throw new UserNotFoundException("id="+id);
//        List<Post> list = user.get().getPosts();
//}



}
