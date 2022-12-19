package com.group2.kahootclone.DTO;

import com.group2.kahootclone.Utils.ResponseUtils;
import com.group2.kahootclone.constant.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseObject<T> {
    @Builder.Default
    private String message = "Successfully";
    private T object;
    @JsonIgnore
    int errorCode;

    public void buildException (String message) {
        errorCode = ErrorCodes.EXCEPTION;
        this.message = message;
        object = null;
    }
    public void buildResourceNotFound (String message) {
        errorCode = ErrorCodes.RESOURCE_NOT_FOUND;
        this.message = message;
        object = null;
    }

    public ResponseEntity<ResponseObject<T>> createResponse(){
        return new ResponseUtils<T>().fromResponseObject(this);
    }
}
