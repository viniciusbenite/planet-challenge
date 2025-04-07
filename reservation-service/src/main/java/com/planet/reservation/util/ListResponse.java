package com.planet.reservation.util;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListResponse {
    private int total;
    private List<?> data;
}
