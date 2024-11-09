package br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderViewModel(String id, int number_order, String status) {
}
