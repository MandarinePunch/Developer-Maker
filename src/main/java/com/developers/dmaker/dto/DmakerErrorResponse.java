package com.developers.dmaker.dto;

import com.developers.dmaker.exception.DmakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmakerErrorResponse {
    private DmakerErrorCode dmakerErrorCode;
    private String errorMessage;
}
