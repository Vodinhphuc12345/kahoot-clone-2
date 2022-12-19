package com.group2.kahootclone.DTO.Response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
public class BaseResponse extends RepresentationModel<BaseResponse> {
    protected long dateCreated;
    protected long dateUpdated;

    public BaseResponse(long dateCreated, long dateUpdated){
        this.dateUpdated = dateUpdated;
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                '}';
    }
}
