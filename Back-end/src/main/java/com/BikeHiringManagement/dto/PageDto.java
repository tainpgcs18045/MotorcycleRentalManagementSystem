package com.BikeHiringManagement.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper = true)
public class PageDto extends BaseDto {
    @SuppressWarnings("rawtypes")
    private List content;
    private long totalElements;
    private int numberOfElements;
    private int totalPages;

    private boolean first;
    private boolean last;

}
