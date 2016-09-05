package hu.poketerkep.shared.api;

import hu.poketerkep.shared.model.UserConfig;
import org.springframework.http.ResponseEntity;

public interface UserAPIEndpoint {
    ResponseEntity<UserConfig> nextUser();

    ResponseEntity<UserConfig> banUser(UserConfig userConfig);

    ResponseEntity<UserConfig> releaseUser(UserConfig userConfig);

    ResponseEntity<UserConfig> addUser(UserConfig userConfig);
}
