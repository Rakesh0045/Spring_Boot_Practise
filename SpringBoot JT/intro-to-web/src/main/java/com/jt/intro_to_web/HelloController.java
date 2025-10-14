package com.jt.intro_to_web;

import java.io.PrintWriter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

@Controller // Stereotype annotation --> that means the Bean of this class will be managed
            // by container
public class HelloController {
    @RequestMapping("/hello")
    public void helloSpring(PrintWriter printWriter) {
        System.out.println("Hello Web");
        printWriter.println("Hello web in browser");
    }

    // @RequestMapping({ "/", "/home" })
    // public void home(PrintWriter printWriter) {
    // printWriter.println("Home Page");
    // }

    @RequestMapping({ "/", "/home" })
    public String home() {
        return "home";
    }

    // @RequestMapping("/contact")
    // public void contactPage(PrintWriter printWriter) {
    // printWriter.println("Contact: 123456789");
    // printWriter.println("devtool works");

    // }

    // @RequestMapping("/contact")
    // public String contactPage() {
    // return "contact";
    // }

    @RequestMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("name", "Rakesh Kumar Parida");
        model.addAttribute("phone", "6370510196");
        return "contact";
    }

    // @RequestMapping("/form" , method = RequestMethod.GET)
    @GetMapping("/form")
    public String form() {
        return "form";
    }

    // @RequestMapping("/submit")
    // public String submitForm(HttpServletRequest request) {
    // String studentName = request.getParameter("name");
    // int studentRoll = Integer.parseInt(request.getParameter("roll"));
    // return "details";
    // }

    // @RequestMapping("/submit")
    // public String submitForm(@RequestParam String name, @RequestParam int roll) {
    // System.out.println(name+"\t"+roll);
    // return "details";
    // }

    // @RequestMapping("/submit")
    // public String submitForm(@RequestParam(value = "name", defaultValue =
    // "default", required = false) String studentName,
    // @RequestParam("roll") int studentRoll) {
    // System.out.println(studentName + "\t" + studentRoll);
    // return "details";
    // }

    //@RequestMapping(value = "/submit", method= RequestMethod.POST)
    @PostMapping("/submit")
    public String submitForm(@ModelAttribute Student student, Model model) {
        model.addAttribute("stud", student);
        return "details";
    }

    @GetMapping("/multi-submit")
    public String showStudents(Model model){
        Student student1 = new Student("Rakesh", 101);
        Student student2 = new Student("Sanoj", 102);
        Student student3 = new Student("Manoj", 103);
        model.addAttribute("students", List.of(student1, student2, student3));
        return "details";
    }

}
