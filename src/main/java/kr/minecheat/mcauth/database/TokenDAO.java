package kr.minecheat.mcauth.database;

import kr.minecheat.mcauth.Server;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TokenDAO {
    public static IntegrationTokenData getTokenDataByTokenA(String tokenA) throws SQLException {
        try (Connection conn = Server.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT a.*,b.nickname,b.username FROM minecraft_integration_token a inner join `user` b on a.user_id = b.id where tokena = ?");
            ps.setString(1, tokenA);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            IntegrationTokenData itd = new IntegrationTokenData();
            itd.setId(rs.getLong("id"));
            itd.setMinecraftUUID(toUUID(rs.getBytes("minecraftuuid")));
            itd.setExpiry_date(rs.getDate("expiry_date"));
            itd.setTokenA(rs.getString("tokena"));
            itd.setTokenB(rs.getString("tokenb"));
            itd.setUser_id(rs.getLong("user_id"));
            itd.setNickname(rs.getString("nickname"));
            itd.setUsername(rs.getString("username"));

            return itd;
        }
    }

    public static boolean updateUUIDAndTokenBByTokenData(IntegrationTokenData itd) throws SQLException {
        try (Connection conn = Server.getDataSource().getConnection()) {
            PreparedStatement ps = conn.prepareStatement("UPDATE minecraft_integration_token SET minecraftuuid=?, tokenb=? where id=?");
            ps.setBytes(1, toBytes(itd.getMinecraftUUID()));
            ps.setString(2, itd.getTokenB());
            ps.setLong(3, itd.getId());

            return 1 == ps.executeUpdate();
        }
    }

    public static boolean deleteToken(IntegrationTokenData itd) throws SQLException {
        try (Connection conn = Server.getDataSource().getConnection()) {

            PreparedStatement ps = conn.prepareStatement("DELETE FROM minecraft_integration_token where id=?");
            ps.setLong(1, itd.getId());

            return 1 == ps.executeUpdate();
        }
    }

    public static UUID toUUID(byte[] bytes) {
        if (bytes == null) return null;
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid;
    }

    public static byte[] toBytes(UUID uuid) {
        if (uuid == null) return null;
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}
