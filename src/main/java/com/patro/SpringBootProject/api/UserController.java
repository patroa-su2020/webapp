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

import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
public class UserController implements ErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    CartService cartService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StatsDClient statsDClient;

    private final static Class<UserController> className = UserController.class;
    private final static Logger logger = LoggerFactory.getLogger(className);

    public UserController() {

    }

    private long startTime;
    private long endTime;

    @RequestMapping(value = {"/forgotPassword"}, method = RequestMethod.GET)
    public ModelAndView getForgotPasswordPage() {
        startTime = System.currentTimeMillis();
//        statsDClient.incrementCounter("endpoint.signup.http.GET");
        ModelAndView mv = new ModelAndView();
        User user = new User();
        mv.addObject("user", user);
        mv.setViewName("forgotPassword");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.forgotPassword.http.GET", endTime - startTime);
//        logger.info("GET /signup >>> Class " + className);
//        logger.error("GET /signup >>> Class " + className);
//        logger.warn("GET /signup >>> Class " + className);


        return mv;
    }

    @RequestMapping(value = {"/forgotPassword"}, method = RequestMethod.POST)
    public ModelAndView createPasswordResetLink(@Valid User user, BindingResult bindingResult) {
        startTime = System.currentTimeMillis();

        ModelAndView mv = new ModelAndView();
        Boolean flag = Boolean.TRUE;
        mv.addObject("flag", flag);

        mv.setViewName("forgotPassword");
        emailService.emailPasswordResetLink(user);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.forgotPassword.http.GET", endTime - startTime);



        return mv;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public ModelAndView getSignupPage() {
        startTime = System.currentTimeMillis();
//        statsDClient.incrementCounter("endpoint.signup.http.GET");
        ModelAndView mv = new ModelAndView();
        User user = new User();
        mv.addObject("user", user);
        mv.setViewName("signup");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.signup.http.GET", endTime - startTime);
        logger.info("GET /signup >>> Class " + className);
        logger.error("GET /signup >>> Class " + className);
        logger.warn("GET /signup >>> Class " + className);


        return mv;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        startTime = System.currentTimeMillis();
        statsDClient.incrementCounter("endpoint.signup.http.POST");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("signup");
        try {

            if (isUserAlreadyRegistered(user.getUsername())) {
                mv.addObject("alreadyRegistered", "User Account Already Exists!");
                endTime = System.currentTimeMillis();
                logger.info("POST /signup: user already exist >>> Class " + className);
                return mv;
            }

            if (!isValidEmail(user.getUsername())) {
                mv.addObject("invalidEmail", "Invalid Email Address");
                endTime = System.currentTimeMillis();
                return mv;
            }

            if (!isPasswordStrong(user.getPassword())) {
                mv.addObject("weakPassword", "Password too weak!");
                endTime = System.currentTimeMillis();
                logger.info("POST /signup: weak password >>> Class " + className);
                return mv;
            } else {
                userService.addUser(user);

                mv.addObject(user);
                mv.addObject("msg", "User has been registered successfully!");
                logger.info("POST /signup: user registration successful >>> Class " + className);
                endTime = System.currentTimeMillis();
                return mv;
            }
        } catch (Exception e) {
            logger.error("POST /signup: weak password >>> Class " + className + "Exception: " + e.getMessage());
            endTime = System.currentTimeMillis();
            return null;
        } finally {
            statsDClient.recordExecutionTime("endpoint.signup.http.POST", endTime - startTime);
        }
    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView getLoginPage() {
        logger.info("GET /login >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        model.setViewName("login");

        Role role1 = new Role();
        role1.setId("1");
        role1.setRole("Seller");
        roleService.addRole(role1);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./login.http.GET", endTime - startTime);
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public ModelAndView validateUser(@Valid User user, BindingResult bindingResult, HttpSession session) {
        logger.info("POST /login >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        Optional<User> backendUserOptional = userService.getUserByUsername(user.getUsername());
        if (!backendUserOptional.isPresent()) {

            model.addObject("noUser", "No user found");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./login.http.POST", endTime - startTime);
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
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./login.http.POST", endTime - startTime);
            return mv;
        } else {
            model.addObject("incorrectPassword", "Incorrect Password");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./login.http.POST", endTime - startTime);
            return model;
        }

    }

    @RequestMapping(value = {"/userDetails"}, method = RequestMethod.GET)
    public ModelAndView getUserDetails(@Valid User user, HttpSession session) {
        logger.info("GET /userDetails >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        User userSessionObj = (User) session.getAttribute("userSession");
        if (userSessionObj == null) {
            mv.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./userDetails.http.GET", endTime - startTime);
            return mv;
        }
        mv.setViewName("userdetails");
        User newUser = userService.getUserByUsername(userSessionObj.getUsername()).get();
        mv.addObject("user", newUser);
        statsDClient.recordExecutionTime("endpoint./userDetails.http.GET", endTime - startTime);
        return mv;
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.GET)
    public ModelAndView getUpdatePageByGetCall(@Valid User user, HttpSession session) {
        logger.info("GET /update >>> Class " + className);
        startTime = System.currentTimeMillis();
        User sessionObj = (User) session.getAttribute("userSession");
        ModelAndView modelAndView = new ModelAndView();
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./update.http.GET", endTime - startTime);
            return modelAndView;
        }

        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./update.http.GET", endTime - startTime);
        return modelAndView;
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ModelAndView getUpdatePage(HttpSession session) {
        logger.info("POST /update >>> Class " + className);
        startTime = System.currentTimeMillis();
        User sessionObj = (User) session.getAttribute("userSession");
        ModelAndView modelAndView = new ModelAndView();
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./update.http.POST", endTime - startTime);
            return modelAndView;
        }

        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./update.http.POST", endTime - startTime);
        return modelAndView;
    }

    @RequestMapping(value = {"/saveupdate"}, method = RequestMethod.GET)
    public ModelAndView getUpdates(@Valid User user, HttpSession session) {
        logger.info("GET /saveupdate >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./saveupdate.http.GET", endTime - startTime);
            return modelAndView;
        }
        User newUser = userService.getUserByUsername(sessionObj.getUsername()).get();
        modelAndView.addObject("user", newUser);
        modelAndView.setViewName("update");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./saveupdate.http.GET", endTime - startTime);
        return modelAndView;
    }

    @RequestMapping(value = {"/saveupdate"}, method = RequestMethod.POST)
    public ModelAndView saveUpdates(@Valid User user, HttpSession session) {
        logger.info("POST /saveupdate >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            modelAndView.setViewName("accessdenied");
            statsDClient.recordExecutionTime("endpoint./saveupdate.http.POST", endTime - startTime);
            endTime = System.currentTimeMillis();
            return modelAndView;
        }
        modelAndView.setViewName("update");
        modelAndView.addObject("user", user);
        if (!isPasswordStrong(user.getPassword())) {
            modelAndView.addObject("weakPassword", "Password too weak!");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./saveupdate.http.POST", endTime - startTime);
            return modelAndView;
        }


        user.setUsername(sessionObj.getUsername());
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userService.updateUserDetails(user);
        modelAndView.addObject("succesfulupdate", "User Details Updated Successfully");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./saveupdate.http.POST", endTime - startTime);
        return modelAndView;
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        logger.info("GET /logout >>> Class " + className);
        startTime = System.currentTimeMillis();
        User u = (User) session.getAttribute("userSession");
        session.invalidate();
        ModelAndView mv = new ModelAndView();
        User user = new User();
        mv.addObject("user", user);

        mv.setViewName("login");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./logout.http.GET", endTime - startTime);
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
        logger.error("error: Internal Server Error  >>> Class " + className);
        startTime = System.currentTimeMillis();
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
        endTime = System.currentTimeMillis();
        return mv;
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }


    @RequestMapping(value = {"/createBook"}, method = RequestMethod.GET)
    public ModelAndView getCreateBookPage(HttpSession session, @Valid BookDTO bookDTO, BindingResult bindingResult) {
        logger.info("GET/createBook  >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView model = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            model.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint./createBook.http.GET", endTime - startTime);
            return model;

        }
        model.setViewName("createBook");
        model.addObject("bookDTO", bookDTO);

        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./createBook.http.GET", endTime - startTime);
        return model;

    }

    @RequestMapping(value = {"/saveUpdateBook/{bookId}/{createDtTm}"}, method = RequestMethod.POST)
    public ModelAndView saveUpdatedBook(@Valid Book updateBook, @PathVariable String bookId, @PathVariable long createDtTm, HttpSession session) {
        logger.info("POST/saveUpdateBook/{bookId}/{createDtTm}  >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView model = new ModelAndView();
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser == null) {
            model.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//saveUpdateBook/{bookId}/{createDtTm}.http.POST", endTime - startTime);
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
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//saveUpdateBook/{bookId}/{createDtTm}.http.POST", endTime - startTime);
        return model;

    }

    @RequestMapping(value = {"/saveBook"}, method = RequestMethod.POST)
    public ModelAndView saveBook(HttpSession session, @Valid Book bookDTO, BindingResult bindingResult, @RequestPart(value = "files") MultipartFile[] files) {
        logger.info("POST/saveBook  >>> Class " + className);
        startTime = System.currentTimeMillis();
        ModelAndView model = new ModelAndView();
        User sessionObj = (User) session.getAttribute("userSession");
        if (sessionObj == null) {
            model.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//saveBook.http.POST", endTime - startTime);
            return model;
        }

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
            logger.info("POST/saveBook  >>> Class >>> " + e.getMessage() + className);
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
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//saveBook.http.POST", endTime - startTime);
        return model;

    }

    @RequestMapping(value = {"/accessdenied"}, method = RequestMethod.GET)
    public ModelAndView getAccessDeniedPage() {
        logger.info("GET/accessdenied  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("accessdenied");
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//accessdenied.http.GET", endTime - startTime);
        return modelAndView;
    }

    @RequestMapping(value = {"/updateBook/{bookId}"}, method = RequestMethod.GET)
    public ModelAndView updateBook(@PathVariable String bookId, HttpSession session) {
        logger.info("GET/updateBook/{bookId}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        long dbStartTime = System.currentTimeMillis();
        Book book = bookService.getBookById(bookId);
        long dbEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB: get book by book Id", dbEndTime - dbStartTime);
        User user = (User) session.getAttribute("userSession");
        if (user == null || !user.getUsername().equals(book.getSellerId())) {
            mv.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//updateBook/{bookId}.http.GET", endTime - startTime);
            return mv;
        }

        mv.setViewName("updateBook");
        mv.addObject("updateBook", book);
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
        Date date1 = new Date(book.getCreateDateTime());
        mv.addObject("createDtTm", simple.format(date1));

        if (book.getUpdateDateTime() > 0) {

            Date date2 = new Date(book.getUpdateDateTime());
            mv.addObject("updateDtTm", simple.format(date2));
        } else
            mv.addObject("updateDtTm", "");


        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//updateBook/{bookId}.http.GET", endTime - startTime);
        return mv;
    }


    @RequestMapping(value = {"/deleteBook/{bookId}"}, method = RequestMethod.GET)
    public void deleteBook(@PathVariable String bookId, HttpServletResponse response, HttpSession session) throws IOException {
        logger.info("GET/deleteBook/{bookId}}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        User user = (User) session.getAttribute("userSession");
        Book book = bookService.getBookById(bookId);
        if (user == null || !book.getSellerId().equals(user.getUsername())) {
            response.sendRedirect("/accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//deleteBook/{bookId}.http.GET", endTime - startTime);
            return;
        }

        if (book != null) {
            long dbStartTime = System.currentTimeMillis();
            List<Image> images = imageService.getImagesByBookId(bookId);
            long dbEndTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("DB Call To get Image URL", dbEndTime - dbStartTime);
            for (Image image : images) {
                imageService.deleteFileFromS3Bucket(image.getImageUrl());
                imageService.deleteImage(image);
            }
            bookService.deleteBookById(bookId);
        }
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//deleteBook/{bookId}.http.GET", endTime - startTime);
        response.sendRedirect("/books");
    }

    @RequestMapping(value = {"/myCart"}, method = RequestMethod.GET)
    public ModelAndView getMyCartPage(HttpSession session) {
        logger.info("GET/myCart}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser == null) {
            mv.setViewName("accessdenied");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//myCart.http.GET", endTime - startTime);
            return mv;
        }
        mv.setViewName("myCart");
        List<Book> backendBooks = bookService.getAllBooks();
        List<Book> myBooks = new ArrayList<>();
        List<CartDTO> cartDtos = new ArrayList<>();
        long dbStartTime = System.currentTimeMillis();
        List<Cart> backendCarts = cartService.getAllCartItems();
        long dbEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB get All Cart Items", dbEndTime - dbStartTime);
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
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//myCart.http.GET", endTime - startTime);
        return mv;

    }


    @RequestMapping(value = {"/addToCart/{bookId}"}, method = RequestMethod.GET)
    public void addToCart(@PathVariable String bookId, HttpServletResponse response, HttpSession session) throws IOException {
        logger.info("GET/addToCart/{bookId}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        User userSession = (User) session.getAttribute("userSession");
        if (userSession == null) {
            response.sendRedirect("/login");
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//addToCart/{bookId}.http.GET", endTime - startTime);
            return;
        }


        List<Cart> backendCarts = cartService.getAllCartItems();

        for (Cart c : backendCarts) {
            if (c.getBookId().equals(bookId) && c.getBuyerId().equals(userSession.getUsername())) {
                c.setQuantity(c.getQuantity() + 1);
                cartService.addBookToCart(c);
                endTime = System.currentTimeMillis();
                statsDClient.recordExecutionTime("endpoint.//addToCart/{bookId}.http.GET", endTime - startTime);
                response.sendRedirect("/books");
                return;
            }
        }

        Cart cart = new Cart();
        cart.setBookId(bookId);
        cart.setBuyerId(userSession.getUsername());
        cart.setCartId(UUID.randomUUID().toString());
        cart.setQuantity(1);
        cartService.addBookToCart(cart);

        bookService.getBookById(bookId);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//addToCart/{bookId}.http.GET", endTime - startTime);
        response.sendRedirect("/myCart");

    }


    private String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @RequestMapping(value = {"/viewImages/{bookId}/{sellerId}"}, method = RequestMethod.GET)
    public ModelAndView getImages(HttpSession session, @PathVariable String bookId, @PathVariable String sellerId) {
        logger.info("GET/viewImages/{bookId}/{sellerId}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        boolean isSeller = false;
        ModelAndView mv = new ModelAndView();
        mv.setViewName("images");
        User sessionUser = (User) session.getAttribute("userSession");
        if (sessionUser != null && sessionUser.getUsername().equals(sellerId))
            isSeller = true;

        List<Image> imageList = imageService.getImagesByBookId(bookId);

        mv.addObject(imageList);
        mv.addObject(bookId);
        mv.addObject("isSeller", isSeller);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//viewImages/{bookId}/{sellerId}.http.GET", endTime - startTime);
        return mv;

    }

    @RequestMapping(value = {"/addNewImage/{bookId}"}, method = RequestMethod.POST)
    public void addNewImages(HttpServletResponse response, HttpSession session, @PathVariable String bookId, @RequestPart(value = "files") MultipartFile[] files) throws IOException {
        logger.info("POST/addNewImage/{bookId}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        Book book = bookService.getBookById(bookId);
        Set<String> fileUrls = null;
        try {
            fileUrls = imageService.uploadPictures(files);
        } catch (IOException e) {
            logger.error("POST/addNewImage/{bookId}  >>> Class >>> " + e.getMessage() + className);
            endTime = System.currentTimeMillis();
            statsDClient.recordExecutionTime("endpoint.//addNewImage/{bookId}.http.POST", endTime - startTime);
            e.printStackTrace();
        }

        Set<Image> imageSet = new HashSet<>();
        for (String url : fileUrls) {
            Image image = new Image();
            image.setImageId(UUID.randomUUID().toString());
            image.setImageUrl(url);
            image.setBookId(bookId);
            imageService.addImages(image);


        }
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//addNewImage/{bookId}.http.POST", endTime - startTime);
        response.sendRedirect("/viewImages/" + bookId + "/" + book.getSellerId());
        return;
    }

    @RequestMapping(value = {"/deleteImage/{imageId}/{bookId}"}, method = RequestMethod.GET)
    public void deleteImage(HttpSession session, @PathVariable String imageId, @PathVariable String bookId, HttpServletResponse response) throws IOException {
        logger.info("GET/deleteImage/{imageId}/{bookId}  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        long dbStartTime = System.currentTimeMillis();
        Image image = imageService.getImageByImageId(imageId);
        long dbEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB Call To get Image By Image Id", dbEndTime - dbStartTime);
        imageService.deleteFileFromS3Bucket(image.getImageUrl());
        imageService.deleteImage(image);
        Book book = bookService.getBookById(bookId);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint.//deleteImage/{bookId}.http.GET", endTime - startTime);
        response.sendRedirect("/viewImages/" + bookId + "/" + book.getSellerId());
        return;

    }

    @RequestMapping(value = {"/books"}, method = RequestMethod.GET)
    public ModelAndView getBooksPage(HttpSession session, @Valid Book book, BindingResult bindingResult) {
        logger.info("GET/books  >>> Class >>> " + className);
        startTime = System.currentTimeMillis();
        ModelAndView mv = new ModelAndView();
        statsDClient.incrementCounter("endpoint./books.http.GET");
        User userSession = (User) session.getAttribute("userSession");
        mv.setViewName("books");
        long dbStartTime = System.currentTimeMillis();
        List<Book> books = bookService.getAllBooks();
        long dbEndTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB get all books", dbEndTime - dbStartTime);

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
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("endpoint./books.http.GET", endTime - startTime);
        return mv;
    }
}
