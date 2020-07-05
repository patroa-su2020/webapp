package com.patro.SpringBootProject.api;

import com.patro.SpringBootProject.model.*;
import com.patro.SpringBootProject.service.*;

import dto.BookDTO;
import dto.CartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
public class UserController implements ErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;


    @Autowired
    private RoleService roleService;

    @Autowired
    CartService cartService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ImageService imageService;

    public UserController() {

    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public ModelAndView getSignupPage() {
        ModelAndView mv = new ModelAndView();
        User user = new User();
        mv.addObject("user", user);
        mv.setViewName("signup");
        return mv;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("signup");
        try {

            if (isUserAlreadyRegistered(user.getUsername())) {
                mv.addObject("alreadyRegistered", "User Account Already Exists!");
                return mv;
            }

            if (!isValidEmail(user.getUsername())) {
                mv.addObject("invalidEmail", "Invalid Email Address");
                return mv;
            }

            if (!isPasswordStrong(user.getPassword())) {
                mv.addObject("weakPassword", "Password too weak!");
                return mv;
            } else {
                userService.addUser(user);

                mv.addObject(user);
                mv.addObject("msg", "User has been registered successfully!");

//                System.out.println(user.getFirstName());
                return mv;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView getLoginPage() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("login");

        Role role1 = new Role();
        role1.setId("1");
        role1.setRole("Seller");
        roleService.addRole(role1);
//        System.out.println(roleService.getAllRole().toString());
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ModelAndView validateUser(@Valid User user, BindingResult bindingResult, HttpSession session) {
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        Optional<User> backendUserOptional = userService.getUserByUsername(user.getUsername());
        if (!backendUserOptional.isPresent()) {
            model.addObject("noUser", "No user found");

            return model;
        }

//        System.out.println("Login Password: " + user.getPassword() + " Encoded Password: " + backendUserOptional.get().getPassword());
        if (BCrypt.checkpw(user.getPassword(), backendUserOptional.get().getPassword())) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("userdetails");
            User backendUser = backendUserOptional.get();
            mv.addObject("user", backendUser);
            session.setAttribute("userSession", backendUser);
            mv.addObject("loginsuccessful", "User Logged In Successfully!");
            return mv;
        } else {
            model.addObject("incorrectPassword", "Incorrect Password");
            return model;
        }

    }

    @RequestMapping(value = {"/userDetails"}, method = RequestMethod.GET)
    public ModelAndView getUserDetails(@Valid User user, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        User userSessionObj = (User) session.getAttribute("userSession");
        if (userSessionObj == null) {
            mv.setViewName("accessdenied");
            return mv;
        }
        mv.setViewName("userdetails");
        User newUser = userService.getUserByUsername(userSessionObj.getUsername()).get();
        mv.addObject("user", newUser);
        return mv;
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.GET)
    public ModelAndView getUpdatePageByGetCall(@Valid User user, HttpSession session) {
        User sessionObj = (User) session.getAttribute("userSession");
        ModelAndView modelAndView = new ModelAndView();
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            return modelAndView;
        }

        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        return modelAndView;
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ModelAndView getUpdatePage(HttpSession session) {
        User sessionObj = (User) session.getAttribute("userSession");
        ModelAndView modelAndView = new ModelAndView();
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            return modelAndView;
        }

        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        return modelAndView;
    }

    @RequestMapping(value = {"/saveupdate"}, method = RequestMethod.GET)
    public ModelAndView getUpdates(@Valid User user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            return modelAndView;
        }
        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        return modelAndView;
    }

    @RequestMapping(value = {"/saveupdate"}, method = RequestMethod.POST)
    public ModelAndView saveUpdates(@Valid User user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            return modelAndView;
        }
        modelAndView.setViewName("update");
        modelAndView.addObject("user", user);
        if (!isPasswordStrong(user.getPassword())) {
            modelAndView.addObject("weakPassword", "Password too weak!");
            return modelAndView;
        }


        user.setUsername(sessionObj.getUsername());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userService.updateUserDetails(user);
        modelAndView.addObject("succesfulupdate", "User Details Updated Successfully");
        return modelAndView;
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        User u = (User) session.getAttribute("userSession");
//        System.out.println("SESSION OBJECT BEFORE invalidating: " + u);
        session.invalidate();
        ModelAndView mv = new ModelAndView();
        User user = new User();
        mv.addObject("user", user);

        mv.setViewName("login");
        return mv;
    }

    public boolean isPasswordStrong(String password) {
        String regularExpression = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(regularExpression);
    }

    private boolean isUserAlreadyRegistered(String username) {
        Optional<User> existingUser = userService.getUserByUsername(username);
        return existingUser.isPresent();

    }

    public boolean isValidEmail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest httpServletRequest) {
        Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                mv.addObject("notFound", "Page Not Found");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                mv.addObject("serverError", "InternalServerError");
            }
        }
        return mv;
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }


    @RequestMapping(value = {"/createBook"}, method = RequestMethod.GET)
    public ModelAndView getCreateBookPage(HttpSession session, @Valid BookDTO bookDTO, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            model.setViewName("accessdenied");
            return model;

        }
        model.setViewName("createBook");
        model.addObject("bookDTO", bookDTO);


        return model;

    }

    @RequestMapping(value = {"/saveUpdateBook/{bookId}/{createDtTm}"}, method = RequestMethod.POST)
    public ModelAndView saveUpdatedBook(@Valid Book updateBook, @PathVariable String bookId, @PathVariable long createDtTm, HttpSession session) {
        ModelAndView model = new ModelAndView();
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser == null) {
            model.setViewName("accessdenied");
            return model;
        }

        model.setViewName("updateBook");
        updateBook.setBookId(bookId);
        updateBook.setSellerName(sessionUser.getFirstName() + " " + sessionUser.getLastName());
        updateBook.setSellerId(sessionUser.getUsername());
        updateBook.setUpdateDateTime(System.currentTimeMillis());
        updateBook.setCreateDateTime(createDtTm);


        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        Date date1 = new Date(updateBook.getCreateDateTime());
        model.addObject("createDtTm", simple.format(date1));

        if (updateBook.getUpdateDateTime() > 0) {

            Date date2 = new Date(updateBook.getUpdateDateTime());
            model.addObject("updateDtTm", simple.format(date2));
        } else
            model.addObject("updateDtTm", "");

//        System.out.println(updateBook);
        model.addObject("saveUpdateSuccess", "Book Details Updated Successfully");
        model.addObject("updateBook", updateBook);
        bookService.saveBook(updateBook);
        return model;

    }

    @RequestMapping(value = {"/saveBook"}, method = RequestMethod.POST)
    public ModelAndView saveBook(HttpSession session, @Valid Book bookDTO, BindingResult bindingResult, @RequestPart(value = "files") MultipartFile[] files) {


        ModelAndView model = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
//        System.out.println(sessionObj);
        if (sessionObj == null) {
            model.setViewName("accessdenied");
            return model;
        }

//        System.out.println(files[1].getOriginalFilename());
//        System.out.println(files[0].getOriginalFilename());


        String bookId = UUID.randomUUID().toString();
        bookDTO.setBookId(bookId);
        bookDTO.setCreateDateTime(System.currentTimeMillis());
        bookDTO.setSellerId(sessionObj.getUsername());
        bookDTO.setSellerName(sessionObj.getFirstName() + " " + sessionObj.getLastName());
        model.setViewName("createBook");
        model.addObject("bookDTO", bookDTO);

//        System.out.println(bookDTO);
        Set<String> fileUrls = null;
        try {
            fileUrls = imageService.uploadPictures(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   if(!fileUrls.isEmpty())  bookDTO.setImageURLs(fileUrls.toString());
//        System.out.println(fileUrls.toString());
        Set<Image> imageSet = new HashSet<>();
        for (String url : fileUrls) {
            Image image = new Image();
            image.setImageId(UUID.randomUUID().toString());
            image.setImageUrl(url);
            image.setBookId(bookId);
            imageService.addImages(image);

        }
        //  bookDTO.setImages(imageSet);
        bookService.saveBook(bookDTO);
        model.addObject("saveSuccess", "New Book Created Successfully");
        return model;

    }

    @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
    public ModelAndView getAccessDeniedPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("accessdenied");
        return modelAndView;
    }

    @RequestMapping(value = {"/updateBook/{bookId}"}, method = RequestMethod.GET)
//    @RequestMapping(value = {"/xyz"}, method = RequestMethod.POST)
    public ModelAndView updateBook(@PathVariable String bookId, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        Book book = bookService.getBookById(bookId);
        User user = (User) session.getAttribute("userSession");
        if (user == null || !user.getUsername().equals(book.getSellerId())) {
            mv.setViewName("accessdenied");
            return mv;
        }
//    public ModelAndView updateBook(@Valid BookDTO bookDTO) {

        mv.setViewName("updateBook");
//        System.out.println("Update Book" + bookId);

        mv.addObject("updateBook", book);
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        Date date1 = new Date(book.getCreateDateTime());
        mv.addObject("createDtTm", simple.format(date1));

        if (book.getUpdateDateTime() > 0) {

            Date date2 = new Date(book.getUpdateDateTime());
            mv.addObject("updateDtTm", simple.format(date2));
        } else
            mv.addObject("updateDtTm", "");


//        System.out.println(book);
        return mv;
    }


    @RequestMapping(value = {"/deleteBook/{bookId}"}, method = RequestMethod.GET)
    // @RequestMapping(value = {"/deleteBook/"}, method = RequestMethod.POST)
//    @RequestMapping(value = {"/xyz"}, method = RequestMethod.POST)

    public void deleteBook(@PathVariable String bookId, HttpServletResponse response, HttpSession session) throws IOException {
        //   public void deleteBook(@Valid Book book, HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("userSession");
        Book book = bookService.getBookById(bookId);
        if (user == null || !book.getSellerId().equals(user.getUsername())) {
            response.sendRedirect("/accessdenied");
            return;
        }

        if (book != null) {
            List<Image> images = imageService.getImagesByBookId(bookId);
            for (Image image : images) {
                imageService.deleteFileFromS3Bucket(image.getImageUrl());
                imageService.deleteImage(image);
            }
            bookService.deleteBookById(bookId);
        }
//        System.out.println("Book Deleted" + book.getTitle());
        response.sendRedirect("/books");
    }

    @RequestMapping(value = {"/myCart"}, method = RequestMethod.GET)
    public ModelAndView getMyCartPage(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser == null) {
            mv.setViewName("accessdenied");
            return mv;
        }
        mv.setViewName("myCart");
        List<Book> backendBooks = bookService.getAllBooks();
        List<Book> myBooks = new ArrayList<>();
        List<CartDTO> cartDtos = new ArrayList<>();
        List<Cart> backendCarts = cartService.getAllCartItems();
        for (Cart cart : backendCarts) {
            if (cart.getBuyerId().equals(sessionUser.getUsername())) {
                // myBooks.add(bookService.getBookById(cart.getBookId()));
                Book b = bookService.getBookById(cart.getBookId());
                if (b == null) continue;
                CartDTO cartDTO = new CartDTO();
                cartDTO.setBook(b);
                cartDTO.setQuantity(cart.getQuantity());
                cartDtos.add(cartDTO);
            }
        }
        mv.addObject("myBooks", myBooks);
        mv.addObject("cartDtos", cartDtos);
        return mv;

    }


    @RequestMapping(value = {"/addToCart/{bookId}"}, method = RequestMethod.GET)
//    @RequestMapping(value = {"/xyz"}, method = RequestMethod.POST)
    public void addToCart(@PathVariable String bookId, HttpServletResponse response, HttpSession session) throws IOException {
//        System.out.println("Add to cart method.....................");
        User userSession = (User) session.getAttribute("userSession");
        if (userSession == null) {
            response.sendRedirect("/login");
            return;
        }


        List<Cart> backendCarts = cartService.getAllCartItems();

        for (Cart c : backendCarts) {
            if (c.getBookId().equals(bookId) && c.getBuyerId().equals(userSession.getUsername())) {
                c.setQuantity(c.getQuantity() + 1);
                cartService.addBookToCart(c);
                response.sendRedirect("/books");
                return;
            }
        }


//        Book book = bookService.getBookById(bookId);
//        System.out.println(book);
//        book.setQuantity(String.valueOf(Integer.parseInt(book.getQuantity()) - 1));
//        bookService.saveBook(book);
//        System.out.println("new book quantity: " + book.getQuantity() + "Book: " + book);
        Cart cart = new Cart();
        cart.setBookId(bookId);
        cart.setBuyerId(userSession.getUsername());
        cart.setCartId(UUID.randomUUID().toString());
        cart.setQuantity(1);
        cartService.addBookToCart(cart);
//        System.out.println("Added To Cart, Book Id : " + bookId);
//        System.out.println("Cart OBJ: " + cart);

        bookService.getBookById(bookId);


        response.sendRedirect("/myCart");

    }


    private String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @RequestMapping(value = {"/viewImages/{bookId}/{sellerId}"}, method = RequestMethod.GET)
    public ModelAndView getImages(HttpSession session, @PathVariable String bookId, @PathVariable String sellerId) {
        boolean isSeller = false;
        ModelAndView mv = new ModelAndView();
        mv.setViewName("images");
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser != null && sessionUser.getUsername().equals(sellerId))
            isSeller = true;
        //   List<Image> imageList = imageService.getImagesByBookId(bookId);
//        System.out.println("Image for book id:" + bookId);
        List<Image> imageList = imageService.getImagesByBookId(bookId);

        mv.addObject(imageList);
        mv.addObject(bookId);
        mv.addObject("isSeller", isSeller);

        return mv;

    }

    @RequestMapping(value = {"/addNewImage/{bookId}"}, method = RequestMethod.POST)
    public void addNewImages(HttpServletResponse response, HttpSession session, @PathVariable String bookId, @RequestPart(value = "files") MultipartFile[] files) throws IOException {

//        System.out.println(bookId);
        Book book = bookService.getBookById(bookId);
        Set<String> fileUrls = null;
        try {
            fileUrls = imageService.uploadPictures(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   if(!fileUrls.isEmpty())  bookDTO.setImageURLs(fileUrls.toString());
//        System.out.println(fileUrls.toString());
        Set<Image> imageSet = new HashSet<>();
        for (String url : fileUrls) {
            Image image = new Image();
            image.setImageId(UUID.randomUUID().toString());
            image.setImageUrl(url);
            image.setBookId(bookId);
            imageService.addImages(image);


        }
        response.sendRedirect("/viewImages/" + bookId + "/" + book.getSellerId());
        return;
    }

    @RequestMapping(value = {"/deleteImage/{imageId}/{bookId}"}, method = RequestMethod.GET)
    public void deleteImage(HttpSession session, @PathVariable String imageId, @PathVariable String bookId, HttpServletResponse response) throws IOException {
        ModelAndView mv = new ModelAndView();
        Image image = imageService.getImageByImageId(imageId);
        imageService.deleteFileFromS3Bucket(image.getImageUrl());
        imageService.deleteImage(image);
        Book book = bookService.getBookById(bookId);
        response.sendRedirect("/viewImages/" + bookId + "/" + book.getSellerId());
        return;

    }

    @RequestMapping(value = {"/books"}, method = RequestMethod.GET)
    public ModelAndView getBooksPage(HttpSession session, @Valid Book book, BindingResult bindingResult) {
        ModelAndView mv = new ModelAndView();
        User userSession = (User) session.getAttribute("userSession");
//        if (userSession == null) {
//            mv.setViewName("accessdenied");
//            return mv;
//        }

        mv.setViewName("books");
        List<Book> books = bookService.getAllBooks();

        if (books.size() > 0) {
            Map<String, List<Book>> studlistGrouped = books.stream()
                    .collect(Collectors.groupingBy(Book::getIsbn, Collectors.toList()));

            books = studlistGrouped.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue().stream().map(Book::getPrice).min(Comparator.naturalOrder()).orElse(Double.valueOf(0))))              //and also sort each group before collecting them in one list
                    .flatMap(e -> e.getValue().stream().sorted(Comparator.comparing(Book::getPrice))).collect(Collectors.toList());
        }

        List<BookDTO> bookDTOS = new ArrayList<>();
        for (Book b : books) {
            // String bookId = book.getBookId();
            List<Image> images = imageService.getImagesByBookId("8c7b6915-aaa2-4c85-aa86-f996ac54ad91");
            if (userSession != null && userSession.getUsername().equals(b.getSellerId())) {
                BookDTO bookDTO = new BookDTO(b, true);
                if (images != null) bookDTO.setImages(images);
                bookDTOS.add(bookDTO);
            } else {
                if (Integer.parseInt(b.getQuantity()) == 0) continue;
                BookDTO bookDTO = new BookDTO(b, false);
                if (images != null) bookDTO.setImages(images);
                bookDTOS.add(bookDTO);
            }

        }

        mv.addObject("bookDtos", bookDTOS);
        return mv;
    }


}
