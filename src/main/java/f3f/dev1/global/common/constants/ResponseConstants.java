package f3f.dev1.global.common.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseConstants {
    public static final ResponseEntity<String> OK = ResponseEntity.ok("OK");
    public static final ResponseEntity<String> DELETE = ResponseEntity.ok("DELETE");
    public static final ResponseEntity<String> CREATE =
            new ResponseEntity<>("CREATED", HttpStatus.CREATED);
    public static final ResponseEntity<Void> FAIL = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    public static final ResponseEntity<String> UPDATE = ResponseEntity.ok("UPDATE");
    public static final ResponseEntity<String> COMPLETE = ResponseEntity.ok("COMPLETE");


}
