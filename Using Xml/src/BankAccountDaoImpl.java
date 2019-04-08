package com.capgemini.bankapp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.bankapp.dao.BankAccountDao;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.util.DbUtil;
import javax.sql.DataSource;

public class BankAccountDaoImpl implements BankAccountDao{
public DataSource dataSource ;
 
    public DataSource getDataSource()
    {
        return this.dataSource;
    }
 
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

	@Override
	public double getBalance(long accountId) {
		String query = "SELECT account_balance FROM bankaccounts WHERE account_id=" + accountId;
		double balance = -1;
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
				ResultSet result = statement.executeQuery()) {
			if (result.next()) {
				balance = result.getDouble(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		}

		return balance;
	}

	@Override
	public void updateBalance(long accountId, double newBalance) {
		String query = "UPDATE bankaccounts SET account_balance=? WHERE account_id=?";
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
			statement.setDouble(1, newBalance);
			statement.setLong(2, accountId);
			int result = statement.executeUpdate();
			System.out.println("No. of rows updated:" + result);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean deleteBankAccount(long account_Id) {
		String query = "DELETE FROM bankaccounts WHERE account_Id=" + account_Id;
		int result;
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
			result = statement.executeUpdate();
			if (result == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean addNewBankAccount(BankAccount account) {
		String query = "INSERT INTO bankaccounts(customer_name,account_type,account_balance)VALUES(?,?,?)";
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement =dataSource.getConnection().prepareStatement(query)) {
			statement.setString(1, account.getAccountHolderName());
			statement.setString(2, account.getAccountType());
			statement.setDouble(3, account.getAccountBalance());
			int result = statement.executeUpdate();
			if (result == 1)
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<BankAccount> findAllBankAccounts() {
		String query = "SELECT * FROM bankaccounts";
		List<BankAccount> accounts = new ArrayList<>();
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
				ResultSet result = statement.executeQuery(query)) {
			while (result.next()) {
				long accountId = result.getLong(1);
				String accountHolderName = result.getString(2);
				String accountType = result.getString(3);
				double accountBalance = result.getDouble(4);

				BankAccount account = new BankAccount(accountId, accountHolderName, accountType, accountBalance);
				accounts.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	@Override
	public BankAccount searchAccount(long accountId) {

		BankAccount account = null;
		String query = "SELECT * FROM bankaccounts WHERE account_id = " + accountId;
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query);
				ResultSet result = statement.executeQuery(query)) {
			if (result.next()) {
				account = new BankAccount(result.getLong(1), result.getString(2), result.getString(3),
						result.getDouble(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	@Override
	public boolean updateBankAccountDetails(long accountId, String accountHolderName, String accountType) {

		String query = "UPDATE bankaccounts SET customer_name=?, account_type=? WHERE account_id=" + accountId;
		//Connection connection = DbUtil.getConnection();
		try (PreparedStatement statement = dataSource.getConnection().prepareStatement(query)) {
			statement.setString(1, accountHolderName);
			statement.setString(2, accountType);
			int result = statement.executeUpdate();
			if (result == 1)
				return true;
			System.out.println("No. of rows updated:" + result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
}
