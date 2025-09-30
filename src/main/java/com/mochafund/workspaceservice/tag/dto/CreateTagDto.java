package com.mochafund.workspaceservice.tag.dto;

import com.mochafund.workspaceservice.tag.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagDto {

    @NotBlank(message = "Tag name must be provided")
    @Size(min = 1, max = 100, message = "Tag name must be between 1 and 100 characters")
    private String name;

    private String description;

    public static Tag fromDto(CreateTagDto tagDto) {
        return Tag.builder()
                .name(tagDto.getName())
                .description(tagDto.getDescription())
                .build();
    }
}
