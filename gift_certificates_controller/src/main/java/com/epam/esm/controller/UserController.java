package com.epam.esm.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.utils.hateoas.UserHateoas;
import com.epam.esm.utils.pagination.Pagination;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests to /user url (contains CRUD operations with users)
 *
 * @author Lizaveta Yakauleva
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
  private final UserService userService;
  private final Pagination pagination;
  private final UserHateoas userHateoas;

  @Autowired
  public UserController(UserService userService, Pagination pagination, UserHateoas userHateoas) {
    this.userService = userService;
    this.pagination = pagination;
    this.userHateoas = userHateoas;
  }

  /**
   * Searches for user with provided name
   *
   * @param name name of the user to find
   * @return founded user
   */
  @GetMapping(value = "/{login}")
  public ResponseEntity<UserDto> findByLogin(@PathVariable("login") String login) {
    UserDto userDto = userService.find(login);
    userHateoas.getLinks(userDto);
    return new ResponseEntity<>(userDto, HttpStatus.OK);
  }

  /**
   * Searches for all existing users
   *
   * @return list with founded users
   */
  @GetMapping
  public ResponseEntity<List<UserDto>> findAll(
      @RequestParam(required = true, name = "page") int page,
      @RequestParam(required = true, name = "pageSize") int pageSize) {
    List<UserDto> userDtos =
        (List<UserDto>) pagination.paginate(userService.findAll(), page, pageSize);
    userDtos.forEach(userHateoas::getLinks);
    return new ResponseEntity<>(userDtos, HttpStatus.OK);
  }
}