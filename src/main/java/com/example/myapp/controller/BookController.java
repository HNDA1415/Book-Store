package com.example.myapp.controller;

import com.example.myapp.ds.Author;
import com.example.myapp.ds.Book;
import com.example.myapp.ds.Category;
import com.example.myapp.service.BookService;
import com.example.myapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;

    //shop
    @GetMapping("/shop/books/details")
    public String bookDetails(@RequestParam("id") int id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        return "book-detail";
    }

    @GetMapping(value = {"/", "/home"})
    public String index(Model model) {
//        model.addAttribute("category",new Category());
//        model.addAttribute("author",new Author());
//        model.addAttribute("authors",bookService.findAllAuthor());
//        model.addAttribute("categories",bookService.findAllCategory());
//        model.addAttribute("book",new Book());
//        model.addAttribute("books",bookService.findAllBook());
        return "home";
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("author", new Author());
        model.addAttribute("authors", bookService.findAllAuthor());
        model.addAttribute("categories", bookService.findAllCategory());
        model.addAttribute("book", new Book());
        model.addAttribute("books", bookService.findAllBook());
        return "dash-board";
    }

    //shop
    @GetMapping("/shop/show-all-books")
    public String listBooks() {
        return "list-books";
    }

    //admin
    @GetMapping("/category-form")
    public String categoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-Form";
    }

    //admin
    @PostMapping("/admin/save-category")
    public String saveCategory(Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "dash-board";
        }
        bookService.saveCategory(category);
        return "redirect:/";
    }

    @PostMapping("/admin/save-author")
    public String saveAuthor(Author author, BindingResult result) {
        if (result.hasErrors()) {
            return "dash-board";
        }
        bookService.saveAuthor(author);
        return "redirect:/";
    }

    //admin
    @PostMapping("/admin/save-book")
    public String saveBook(Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "dash-board";
        }
        bookService.saveBook(book, book.getCategoryId(), book.getAuthorId());
        return "redirect:/";
    }

    //admin
    @GetMapping("/admin/list-category")
    public String listCategory(Model model) {
        model.addAttribute("categories", bookService.findAllCategory());
        return "list-Category";
    }

    @GetMapping("/admin/list-books")
    public String listAllBooks(Model model) {
        model.addAttribute("books", bookService.findAllBook());
        return "post-list";
    }

    @GetMapping("/admin/list-author")
    public String listAuthor(Model model) {
        model.addAttribute("authors", bookService.findAllAuthor());
        return "list-Author";
    }

    @ModelAttribute("allbooks")
    public List<Book> showAllBooks() {
        return bookService.findAllBook();
    }

    @ModelAttribute("cartSize")
    public int cartSize() {
        return cartService.cartSize();
    }
}
