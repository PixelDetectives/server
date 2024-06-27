package site.pixeldetective.server.dao;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.RoomDTO;
import site.pixeldetective.server.dto.TestDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private static Connection conn = DBConnector.getConnection();

    // 조회
    public List<RoomDTO> getAllRooms() {
        List<RoomDTO> roomDTOList = new ArrayList<>();
        String sql = "select * from room";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roomDTOList.add(new RoomDTO(
                        rs.getInt("r_num"),
                        rs.getInt("r_player1"),
                        rs.getInt("r_player2"),
                        rs.getString("r_name"),
                        rs.getString("r_difficulty"),
                        rs.getInt("g_num")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomDTOList;
    }

    // 생성
    public int createRoom(RoomDTO roomDTO) {
        String sql = "insert into room values (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, roomDTO.getR_roomID());
            pstmt.setInt(1, roomDTO.getU_num_player1());
            pstmt.setInt(2, roomDTO.getU_num_player2());
            pstmt.setString(3, roomDTO.getR_name());
            pstmt.setString(4, roomDTO.getR_difficulty());
            pstmt.setInt(5, roomDTO.getG_num());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
