package dev.hiok.portfolioresourceserver.api.status.openapi;

import dev.hiok.portfolioresourceserver.api.status.model.StatusResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Status")
public interface StatusControllerOpenApi {
  
  @ApiOperation("Get status")
  StatusResponse status();

}
