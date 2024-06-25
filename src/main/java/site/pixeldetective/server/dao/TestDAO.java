package site.pixeldetective.server.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import site.pixeldetective.server.dto.TestDTO;
import site.pixeldetective.server.db.DBConnector;

public class TestDAO {
    private static Connection conn = DBConnector.getConnection();

    public TestDAO() {

    }
    public List<TestDTO> getAllTests() {
        List<TestDTO> testDataList = new ArrayList<>();
        String sql = "select * from Test";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("name");
                testDataList.add(new TestDTO(id, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return testDataList;
    }

    public TestDTO getTestById(int id) {
        String sql = "select * from Test where Id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                return new TestDTO(id, name);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int createTest(TestDTO testData) {
        String sql = "insert into Test (name) values (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, testData.getName());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTest(TestDTO testData) {
        String sql = "update Test set name = ? where Id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, testData.getName());
            pstmt.setInt(2, testData.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTest(int id) {
        String sql = "delete from Test where Id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
