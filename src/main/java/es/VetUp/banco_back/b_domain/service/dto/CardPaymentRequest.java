package es.VetUp.banco_back.b_domain.service.dto;

import es.VetUp.banco_back.b_domain.service.dto.smallDto.Authorization;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Destination;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Origin;
import es.VetUp.banco_back.b_domain.service.dto.smallDto.Pay;

public record
CardPaymentRequest(
        Authorization authorization,
        Origin origin,
        Destination destination,
        Pay payment
) {
}
