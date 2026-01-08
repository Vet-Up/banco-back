package es.VetUp.banco_back.a_presentation.webModel.response;

public record UserSummaryResponse(
        Long userId,
        String username,
        String password,
        String name,
        String firstSurname,
        String secondSurname,
        String dni,
        String apiKey
) {
}
