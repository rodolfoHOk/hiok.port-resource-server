package dev.hiok.portfolioresourceserver.api.exceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import dev.hiok.portfolioresourceserver.domain.exception.BusinessException;
import dev.hiok.portfolioresourceserver.domain.exception.EntityInUseException;
import dev.hiok.portfolioresourceserver.domain.exception.EntityNotFoundException;
import dev.hiok.portfolioresourceserver.domain.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
  
  @Autowired
  private MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    if (body == null) {
      body = createProblemDetails(ProblemType.SYSTEM_ERROR, status, status.getReasonPhrase());
    } else if (body instanceof String) {
      body = createProblemDetails(ProblemType.SYSTEM_ERROR, status, (String) body);
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {
    var status = HttpStatus.BAD_REQUEST;
    var body = createProblemDetails(ProblemType.BUSINESS_ERROR, status, ex.getMessage());

    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    var status = HttpStatus.NOT_FOUND;
    var body = createProblemDetails(ProblemType.RESOURCE_NOT_FOUND, status, ex.getMessage());

    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityInUseException.class)
  public ResponseEntity<?> handleEntityInUseException(EntityInUseException ex, WebRequest request) {
    var status = HttpStatus.CONFLICT;
    var body = createProblemDetails(ProblemType.ENTITY_IN_USE, status, ex.getMessage());

    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<?> handleValidationException(ValidationException ex, WebRequest request) {
    var status = HttpStatus.BAD_REQUEST;

    ProblemDetails body = null;
    if (ex.getBindingResult() != null) {
      body = createProblemDetailsWithInvalidParams(ex.getBindingResult(), status);
    } else {
      body = createProblemDetails(ProblemType.INVALID_DATA, status, ex.getMessage());
    }

    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

//  @ExceptionHandler(AccessDeniedException.class)
//  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//    var status = HttpStatus.FORBIDDEN;
//    var body = createProblemDetails(ProblemType.ACCESS_DENIED, status, ex.getMessage());
//
//    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
//  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUncaughtException(Exception ex, WebRequest request) {
    var status = HttpStatus.INTERNAL_SERVER_ERROR;
    var body = createProblemDetails(ProblemType.SYSTEM_ERROR, status, 
      "An unexpected internal system error has occurred");

    log.error(ex.getMessage(), ex);

    return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    var body = createProblemDetailsWithInvalidParams(ex.getBindingResult(), status);

    return handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    var body = createProblemDetailsWithInvalidParams(ex.getBindingResult(), status);

    return handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String detail = String.format("Resource %s not exist", ex.getRequestURL());
    var body = createProblemDetails(ProblemType.RESOURCE_NOT_FOUND, status, detail);  
      
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		if (ex instanceof MethodArgumentTypeMismatchException) {
			String detail = String.format(
				"The informed url parameter '%s' is not of the expected type %s.",
			  ((MethodArgumentTypeMismatchException) ex).getParameter().getParameterName(),
			  ex.getRequiredType().getSimpleName());
			var body = createProblemDetails(ProblemType.INVALID_PARAMETER, status, detail);
			
			return handleExceptionInternal(ex, body, headers, status, request);
		}

		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		String detail = "The request body is invalid. Verify syntax error";
		
		if (rootCause instanceof InvalidFormatException) {
			InvalidFormatException causeEx = (InvalidFormatException) rootCause;
			String path = joinPath(causeEx.getPath());
			detail = String.format(
        "The informed property '%s' is not of the expected type %s.",
				path, causeEx.getTargetType().getSimpleName());
			
		} else if (rootCause instanceof PropertyBindingException) {
			PropertyBindingException causeEx = (PropertyBindingException) rootCause;
			String path = joinPath(causeEx.getPath());
			detail = String.format("The property '%s' not exist", path);
		}
		
		var body = createProblemDetails(ProblemType.INCOMPREHENSIBLE_MESSAGE, status, detail);
		
		return handleExceptionInternal(ex, body, headers, status, request);
	}

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    var body = createProblemDetails(ProblemType.UNSUPPORTED_MEDIA_TYPE, status, ex.getMessage());
        
    return handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    
    return ResponseEntity.status(status).headers(headers).build();
  }

  private ProblemDetails createProblemDetails(ProblemType problemType, HttpStatus status, String detail) {
    return ProblemDetails.builder()
      .type(problemType.getType())
      .title(problemType.getTitle())
      .status(status.value())
      .detail(detail)
      .timestamp(OffsetDateTime.now())
      .build();
  }

  private ProblemDetails createProblemDetailsWithInvalidParams(BindingResult bindingResult, HttpStatus status) {
    ProblemType problemType = ProblemType.INVALID_DATA;
    String detail = "One or more fields are invalid";

    List<ProblemDetails.InvalidParams> invalidParams = bindingResult.getAllErrors().stream()
      .map(objectError -> {
        String reason = messageSource.getMessage(objectError, Locale.US);
        String name = objectError.getObjectName();
        if (objectError instanceof FieldError) {
          name = ((FieldError) objectError).getField();
        }

        return ProblemDetails.InvalidParams.builder()
          .name(name)
          .reason(reason)
          .build();
      }).collect(Collectors.toList());

    return ProblemDetails.builder()
      .type(problemType.getType())
      .title(problemType.getTitle())
      .status(status.value())
      .detail(detail)
      .timestamp(OffsetDateTime.now())
      .invalidParams(invalidParams)
      .build();
  }

  private String joinPath(List<Reference> references) {
		return references.stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));
	}

}
