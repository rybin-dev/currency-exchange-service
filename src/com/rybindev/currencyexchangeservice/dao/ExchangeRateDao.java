package com.rybindev.currencyexchangeservice.dao;

import com.rybindev.currencyexchangeservice.exception.DaoException;
import com.rybindev.currencyexchangeservice.model.ExchangeRate;
import com.rybindev.currencyexchangeservice.util.ConnectionManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExchangeRateDao {
    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    private static final String DELETE_SQL = "DELETE FROM exchange_rate WHERE id = ?";

    private static final String SAVE_SQL = "INSERT INTO exchange_rate(base_id, target_id, rate) VALUES(?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE exchange_rate SET base_id = ?, target_id = ?, rate = ? WHERE id = ?";

    private static final String FIND_ALL = "SELECT er.id, base_id, target_id, rate FROM exchange_rate er";

    private static final String FIND_BY_ID = FIND_ALL + " WHERE id = ?";

    private static final String FIND_BY_BASE_CODE_AND_TARGET_CODE_SQL = FIND_ALL + " JOIN currency bc ON base_id = bc.id AND bc.code = ? JOIN currency tc ON target_id = tc.id AND tc.code = ? ";

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }


    public Optional<ExchangeRate> findByBaseCodeAndTargetCode(String baseCode, String targetCode) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_BASE_CODE_AND_TARGET_CODE_SQL)) {
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DaoException(e);
        }


    }

    public List<ExchangeRate> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<ExchangeRate> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Optional<ExchangeRate> findById(Integer id, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;
            if (resultSet.next()) {
                exchangeRate = buildExchangeRate(resultSet);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public ExchangeRate update(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, exchangeRate.getBase().getId());
            preparedStatement.setInt(2, exchangeRate.getTarget().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());
            preparedStatement.setInt(4, exchangeRate.getId());

            preparedStatement.executeUpdate();
            return exchangeRate;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, exchangeRate.getBase().getId());
            preparedStatement.setInt(2, exchangeRate.getTarget().getId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                exchangeRate.setId(generatedKeys.getInt(1));
            }

            return exchangeRate;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return ExchangeRate.builder()
                .id(resultSet.getInt("id"))
                .base(currencyDao.findById(
                        resultSet.getInt("base_id"),
                        resultSet.getStatement().getConnection()).orElse(null))
                .target(currencyDao.findById(
                        resultSet.getInt("target_id"),
                        resultSet.getStatement().getConnection()).orElse(null))
                .rate(resultSet.getBigDecimal("rate"))
                .build();
    }
}
