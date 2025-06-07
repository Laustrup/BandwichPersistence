package laustrup.bandwichpersistence.web.services;

import laustrup.bandwichpersistence.core.models.Response;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public abstract class WebService {

    public static <DTO> ResponseEntity<DTO> respond(
            Supplier<Response<DTO>> manager
    ) {
        Response<DTO> response = manager.get();

        return new ResponseEntity<>(
                response.get_object(),
                response.get_httpStatus()
        );
    }
}
