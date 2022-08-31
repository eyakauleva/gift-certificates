package com.epam.esm.dto;

import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

/**
 * Role DTO
 *
 * @author Lizaveta Yakauleva
 * @version 1.0
 */
public class RoleDto extends RepresentationModel<RoleDto> {
  private Long id;

  @NotBlank(message = "Name should not be blank")
  @Size(min = 2, max = 50, message = "Name length should be between 2 and 50")
  private String name;

  public RoleDto() {}

  public RoleDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RoleDto roleDto = (RoleDto) o;
    return Objects.equals(id, roleDto.id) && Objects.equals(name, roleDto.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "RoleDto{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
