package com.epam.esm.controller.order;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.hateoas.OrderHateoas;
import com.epam.esm.utils.pagination.Pagination;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests to /order url (contains CRUD operations with orders)
 *
 * @author Lizaveta Yakauleva
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {
  private final OrderService orderService;

  private final UserService userService;
  private final Pagination pagination;
  private final OrderHateoas orderHateoas;

  @Autowired
  public OrderController(
      OrderService orderService,
      UserService userService,
      Pagination pagination,
      OrderHateoas orderHateoas) {
    this.orderService = orderService;
    this.userService = userService;
    this.pagination = pagination;
    this.orderHateoas = orderHateoas;
  }

  /**
   * Searches for all user's orders
   *
   * @param userId id of the user whose orders to find
   * @return ResponseEntity containing http status and list of founded orders
   */
  @GetMapping(value = "/user/{userId}")
  public ResponseEntity<List<OrderDto>> findByUserId(
      @PathVariable("userId") Long userId,
      @RequestParam(required = true, name = "page") int page,
      @RequestParam(required = true, name = "pageSize") int pageSize) {
    List<OrderDto> orderDtos =
        (List<OrderDto>) pagination.paginate(orderService.findByUserId(userId), page, pageSize);
    orderDtos.forEach(orderHateoas::getLinks);
    return new ResponseEntity<>(orderDtos, HttpStatus.OK);
  }

  /**
   * Searches for order's cost and timestamp of a purchase by order's id
   *
   * @param id id of the user whose orders to find
   * @return ResponseEntity containing http status and founded order info
   */
  @GetMapping(value = "/{id}")
  public ResponseEntity<OrderInfo> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(new OrderInfo(orderService.findById(id)), HttpStatus.OK);
  }

  /**
   * Creates new order
   *
   * @param orderDto order to create
   * @return ResponseEntity containing only http status (without body)
   * @throws AccessDeniedException when authenticated user tries to make an order for another user
   */
  @PostMapping(value = "/user/{userId}")
  public ResponseEntity<Void> create(
      @RequestBody OrderDto orderDto, @PathVariable("userId") Long userId) {
    UserDto userDto =
        userService.find(SecurityContextHolder.getContext().getAuthentication().getName());
    if (!userDto.getId().equals(userId))
      throw new AccessDeniedException("Attempt to make an order for another user");
    orderService.create(orderDto, userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
