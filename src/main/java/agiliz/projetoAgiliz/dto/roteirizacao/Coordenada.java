package agiliz.projetoAgiliz.dto.roteirizacao;


import jakarta.persistence.Embeddable;

@Embeddable
public record Coordenada(Double latitude, Double longitude) {}
