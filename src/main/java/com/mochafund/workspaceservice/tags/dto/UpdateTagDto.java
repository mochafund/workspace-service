package com.mochafund.workspaceservice.tags.dto;

import com.mochafund.workspaceservice.tags.enums.TagStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTagDto {

    @Size(min = 1, max = 100, message = "Tag name must be between 1 and 100 characters")
    private String name;

    @Size(min = 1, max = 255, message = "Tag description must be between 1 and 255 characters")
    private String description;

    private TagStatus status;

}
