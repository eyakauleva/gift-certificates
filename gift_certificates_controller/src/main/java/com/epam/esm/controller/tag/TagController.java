package com.epam.esm.controller.tag;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.MostWidelyUsedTagService;
import com.epam.esm.service.TagService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles requests to /tag url
 * (contains CRD operations with tags)
 *
 * @author Lizaveta Yakauleva
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/tag")
public class TagController {
  private static final Logger logger = Logger.getLogger(TagController.class);
  private final TagService tagService;
  private final MostWidelyUsedTagService mostWidelyUsedTagService;

  @Autowired
  public TagController(TagService tagService, MostWidelyUsedTagService mostWidelyUsedTagService) {
    this.tagService = tagService;
    this.mostWidelyUsedTagService = mostWidelyUsedTagService;
  }

  /**
   * Creates new tag
   * @param tagDto tag to create
   */
  @PostMapping
  public void create(@RequestBody TagDto tagDto)  { //throws ResourceAlreadyExistExcepton
    tagService.create(tagDto);
  }

  /**
   * Deletes existing tag
   * @param id id of the tag to delete
   */
  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable("id") int id) {
    tagService.delete(id);
  }

  /**
   * Searches for tags
   * <br>if {@code name} is null, method searches for all tags, otherwise for the tag with provided name
   * @param name name of the tag to find
   * @return list with founded tags
   */
  @GetMapping
  public List<TagDto> find(@RequestParam(required = false, name = "name") String name) {
    if (Objects.isNull(name)) return tagService.findAll();
    else return Collections.singletonList(tagService.find(name));
  }

  /**
   * Searches for the most widely used tag of a user with the highest cost of all orders
   *
   * @return founded tag
   */
  @GetMapping(value = "/find")
  public TagDto findTag() {
    return mostWidelyUsedTagService.findTag();
  }
}
