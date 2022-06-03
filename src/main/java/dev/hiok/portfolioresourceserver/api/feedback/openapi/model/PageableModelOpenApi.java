package dev.hiok.portfolioresourceserver.api.feedback.openapi.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel("Pageable")
@Getter
@Setter
public class PageableModelOpenApi {
  
  @ApiModelProperty(value = "Page number (starts at 0)", example = "0")
  private Integer page;

  @ApiModelProperty(value = "Elements per page" , example = "9")
  private Integer size;

  @ApiModelProperty(value = "Property name for sorting, asc or desc", example = "name,desc")
  private List<String> sort;

}
