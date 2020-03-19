package kr.minecheat.mcauth.database;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class IntegrationTokenData {
    private long id;
    private UUID minecraftUUID;
    private Date expiry_date;
    private String tokenA;
    private String tokenB;
    private long user_id;
    private String nickname;
    private String username;
}
