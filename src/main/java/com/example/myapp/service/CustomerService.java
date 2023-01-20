package com.example.myapp.service;

import com.example.myapp.dao.CustomerBookOrderDao;
import com.example.myapp.dao.CustomerDao;
import com.example.myapp.dao.RolesDao;
import com.example.myapp.ds.BookDto;
import com.example.myapp.ds.Customer;
import com.example.myapp.ds.CustomerBookOrder;
import com.example.myapp.ds.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CartService cartService;
    @Autowired
    private RolesDao rolesDao;
    @Autowired
    private CustomerBookOrderDao customerBookOrderDao;

    public Customer findCustomerByName(String name){
        return customerDao.findCustomerByName(name).orElse(null);
    }

    @Transactional
    public void register(Customer customer, Set<BookDto> bookDtoList) {
        Roles roles = rolesDao.findRolesByRoleName("ROLE_USER").get();
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.addRole(roles);
        for (BookDto bookDto : bookDtoList) {
            customer.addBook(cartService.toEntity(bookDto));
        }
        Customer customer1 = customerDao.saveAndFlush(customer);

        saveCustomerBookOrder( customer1, bookDtoList);
    }

    public void saveCustomerBookOrder(Customer customer1, Set<BookDto> bookDtoList) {
        CustomerBookOrder customerBookOrder = new CustomerBookOrder();
        customerBookOrder.setCustomer(customer1);
        customerBookOrder.setTotalAmount(totalPrices(bookDtoList));
        customerBookOrder.setOrderCode(generateCode(customer1));
        customerBookOrderDao.save(customerBookOrder);
    }

    private String generateCode(Customer customer){
        Random random=new Random();
        int code= random.nextInt(100000) + 10000;
        return customer.getName()+code;
    }

    private double totalPrices(Set<BookDto> books) {
        return books
                .stream()
                .map(b -> b.getPrice() * b.getQuantity())
                .mapToDouble(d -> d)
                .sum();
    }
}
