package com.group2.kahootclone.Utils;

import com.group2.kahootclone.constant.ErrorCodes;
import com.group2.kahootclone.DTO.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils<T> {
    public ResponseEntity<ResponseObject<T>> fromResponseObject(ResponseObject<T> responseObject) {
        int err = responseObject.getErrorCode();

        switch (err) {
            case ErrorCodes.SUCCESS:
                return new ResponseEntity<>(responseObject, HttpStatus.OK);
            case ErrorCodes.EXCEPTION:
                return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
            case ErrorCodes.READ_FAILED:
            case ErrorCodes.RESOURCE_NOT_FOUND:
                return new ResponseEntity<>(responseObject, HttpStatus.NOT_FOUND);
            case ErrorCodes.WRITE_FAILED:
                return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
            case ErrorCodes.EXISTED:
                return new ResponseEntity<>(responseObject, HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(responseObject, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
