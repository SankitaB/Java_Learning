package com.example.learn_jpa_and_hibernate.course;

import com.example.learn_jpa_and_hibernate.course.jpa.CourseJpaRepository;
import com.example.learn_jpa_and_hibernate.course.springdatajpa.CourseSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CourseCommandLineRunner implements CommandLineRunner {


//    @Autowired
//    private CourseJpaRepository repository;

//    @Autowired
//    private CourseJdbcRepository repository;
//
    @Autowired
    private CourseSpringDataJpaRepository repository;

    @Override
    public void run(String... args) {
        repository.save(new Course(1, "Learn AWS now!", "in28Minutes"));
        repository.save(new Course(2, "Learn Java now!", "in28Minutes"));
        repository.save(new Course(3, "Learn Azure Today!", "in28Minutes"));

        repository.deleteById(2l);
        System.out.println(repository.findById(1l));
        System.out.println(repository.findById(3l));

        System.out.println(repository.findAll());
        System.out.println(repository.count());
        System.out.println(repository.findByAuthor("in28Minutes"));
        System.out.println(repository.findByAuthor(""));
        System.out.println(repository.findByName("Learn AWS now!"));
        System.out.println(repository.findByName("Learn Java now!"));

    }
}
