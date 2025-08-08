package com.manindra.management.Services;

import com.manindra.management.Product.Post;
import com.manindra.management.Product.StudentDetails;
import com.manindra.management.Repo.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServices {

    @Autowired
    private StudentRepo repo;

    public List<StudentDetails> getAllStudent() {
        return repo.findAll();
    }

    public StudentDetails getStudentById(int id) {
        return repo.findById(id).orElse(null);
    }

    public void saveStudent(StudentDetails student) {
        repo.save(student);
    }

    public void deleteStudent(int id) {
        repo.deleteById(id);
    }

    public StudentDetails getUpdated(int id, StudentDetails student) {
        StudentDetails existing = repo.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(student.getName());
            existing.setGender(student.getGender());
            existing.setEmail(student.getEmail());
            existing.setPhoneno(student.getPhoneno());
            existing.setYear(student.getYear());
            existing.setBranch(student.getBranch());
            existing.setSkill(student.getSkill());
            return repo.save(existing);
        }
        return null;
    }

    public List<StudentDetails> searchStudents(String query) {
        return repo.searchAcrossFields(query);
    }

    // Save a post under a specific student
    public void savePost(Post post) {
        StudentDetails student = getStudentById(post.getStudent().getId());
        if (student != null) {
            post.setStudent(student);
            if (student.getPosts() == null) {
                student.setPosts(new ArrayList<>());
            }
            student.getPosts().add(post);
            repo.save(student);
        }
    }

    // Get all posts by student
    public List<Post> getPostsByStudentId(int studentId) {
        StudentDetails student = getStudentById(studentId);
        return (student != null && student.getPosts() != null) ? student.getPosts() : new ArrayList<>();
    }

    // Get binary image data for preview
    public byte[] getImageByPostId(int postId) {
        List<StudentDetails> allStudents = repo.findAll();
        for (StudentDetails student : allStudents) {
            if (student.getPosts() != null) {
                for (Post post : student.getPosts()) {
                    if (post.getId() == postId && post.getFileData() != null) {
                        return post.getFileData();
                    }
                }
            }
        }
        return new byte[0];
    }
    public void deletePostById(int postId) {
        for (StudentDetails student : repo.findAll()) {
            List<Post> posts = student.getPosts();
            if (posts != null) {
                posts.removeIf(post -> post.getId() == postId);
                repo.save(student);
            }
        }
    }

}
