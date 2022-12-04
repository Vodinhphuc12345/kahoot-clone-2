package com.group2.kahootclone.object.Request.presentationController;

import com.group2.kahootclone.Utils.MapperUtil;
import com.group2.kahootclone.model.Presentation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PresentationRequest {
    @NotBlank(message = "Presentation name is mandatory")
    @NotNull(message = "Presentation name is not null")
    String presentationName;
    @NotBlank(message = "Presentation description is mandatory")
    @NotNull(message = "Presentation description is not null")
    String description;

    public Presentation toPresentation (){
        return MapperUtil.INSTANCE.map(this, Presentation.class);
    }
}
