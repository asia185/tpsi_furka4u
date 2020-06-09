package wizut.tpsi.furka4u.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class HttpAccessDenied extends RuntimeException {

    public HttpAccessDenied() {
        super("Access denied!");
    }
}
