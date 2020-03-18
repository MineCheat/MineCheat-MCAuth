package kr.minecheat.mcauth.mcdata;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserData {
    private String serverURL;

    private String username;

    private byte[] shared_secret;

    private UUID uid;

    private List<MojangUser.MojangUserProperties> properties;
}
