package com.manindra.management.Controller;

import com.manindra.management.Product.Post;
import com.manindra.management.Product.StudentDetails;
import com.manindra.management.Services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    private StudentServices services;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/students")
    public String showStudentsPage(Model model) {
        model.addAttribute("students", services.getAllStudent());
        return "students";
    }

    @GetMapping("/students/create")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new StudentDetails());
        return "create";
    }

    @PostMapping("/students/create")
    public String handleCreateStudent(@ModelAttribute StudentDetails student) {
        services.saveStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable int id, Model model) {
        StudentDetails student = services.getStudentById(id);
        model.addAttribute("student", student);
        return "edit";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        services.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/search")
    public String searchStudents(@RequestParam("query") String query, Model model) {
        List<StudentDetails> results = services.searchStudents(query);
        model.addAttribute("students", results);
        return "students";
    }

//    @GetMapping("/posts/create/{studentId}")
//    public String showPostForm(@PathVariable int studentId, Model model) {
//        StudentDetails student = services.getStudentById(studentId);
//        Post post = new Post();
//        post.setStudent(student);
//        model.addAttribute("post", post);
//        return "create_post";
//    }
@GetMapping({"/posts/create/{studentId}", "/students/{studentId}/post"})
public String showPostForm(@PathVariable int studentId, Model model) {
    StudentDetails student = services.getStudentById(studentId);
    Post post = new Post();
    post.setStudent(student);
    model.addAttribute("post", post);
    return "create_post";
}


    @PostMapping("/posts/save")
    public String savePost(@ModelAttribute Post post,
                           @RequestParam("file") MultipartFile file) throws IOException {
        post.setTimestamp(LocalDateTime.now());
        if (!file.isEmpty()) {
            post.setFileData(file.getBytes());
            post.setFileName(file.getOriginalFilename());
            post.setFileType(file.getContentType());
        }
        services.savePost(post);
        return "redirect:/students";
    }

    @GetMapping("/posts/view/{studentId}")
    public String viewPosts(@PathVariable int studentId, Model model) {
        model.addAttribute("posts", services.getPostsByStudentId(studentId));
        return "posts";
    }

    @GetMapping("/posts/image/{postId}")
    @ResponseBody
    public byte[] serveImage(@PathVariable int postId) {
        return services.getImageByPostId(postId);
    }
    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable int id) {
        services.deletePostById(id);
        return "redirect:/students"; // or redirect back to the specific student post list
    }




}
