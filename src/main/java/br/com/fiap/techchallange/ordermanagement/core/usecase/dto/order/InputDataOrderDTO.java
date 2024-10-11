package br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order;

import java.util.List;

public record InputDataOrderDTO(String id, List<InputDataItemDTO> items) {}
