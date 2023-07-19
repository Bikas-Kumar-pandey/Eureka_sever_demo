package com.eureka.student.controller;

import com.eureka.student.dto.Student;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("v1/student")
public class StudentController {
    @Autowired
    public RestTemplate restTemplate;

    @GetMapping("detail")
    public String getTeacherDetailsFromFeign() {
        RestTemplate restTemplate = new RestTemplate();
//        String returnTeacherDetails = restTemplate.getForObject("http://teacher-service/v1/teacher/subjects", String.class);
        String returnTeacherDetails = restTemplate.getForObject("http://localhost:9091/v1/teacher/subjects", String.class);
        return returnTeacherDetails;
    }

    @GetMapping("detailF")
    @HystrixCommand(fallbackMethod = "handleTeacherDownTime")
    public String getTeacherDetailsFromFeignToFail() {
//        It give 500 eroor coz not handled when error occurs at other microservice
        RestTemplate restTemplate = new RestTemplate();
        String returnTeacherDetails = restTemplate.getForObject("http://teacher-service/v1/teacher/fail", String.class);
        return returnTeacherDetails;
    }

    public String handleTeacherDownTime() {
        String normalValue = "Hello maybe fail";
        return normalValue;
    }

    @PostMapping("details")
    public String setStudentDetails(@RequestBody Student student) {
        String name = student.getName();
        String grade = student.getGrade();
        return "Name is = " + name + "and Grade is = " + grade;
    }

    @GetMapping("details")
    public String getStudentDetails() {
        return "Name is = Bikas" + "and Grade is = Phd";
    }
}
