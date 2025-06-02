package com.zju.main.sharingSystem.util.typehandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@MappedTypes(List.class)
public class IntegerListTypeHandler implements TypeHandler<List<Integer>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Integer> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, java.sql.Types.VARCHAR);
        } else {
            try {
                ps.setString(i, objectMapper.writeValueAsString(parameter));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert List<Integer> to JSON string", e);
            }
        }
    }

    @Override
    public List<Integer> getResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to List<Integer>", e);
        }
    }

    @Override
    public List<Integer> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to List<Integer>", e);
        }
    }

    @Override
    public List<Integer> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON string to List<Integer>", e);
        }
    }
}