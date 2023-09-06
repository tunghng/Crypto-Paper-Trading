package com.tma.helper.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoggerDto {
    private String actionData;
    private String actionType;
    private String actionState;
}
