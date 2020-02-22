package com.money.transfer;

import com.money.transfer.controller.AccountController;
import com.money.transfer.dao.AccountDao;
import com.money.transfer.exception.BusinessException;
import com.money.transfer.mapper.AccountMapper;
import com.money.transfer.model.Account;
import com.money.transfer.service.AccountService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Optional;

public class MoneyTransferApplication extends Application<ApplicationConfiguration> {

    private HibernateBundle<ApplicationConfiguration> hibernateBundle = new HibernateBundle<ApplicationConfiguration>(Account.class) {
        public DataSourceFactory getDataSourceFactory(ApplicationConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "Money Transfer Application";
    }

    @Override
    public void initialize(final Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    public void run(ApplicationConfiguration configuration, Environment environment) {
        final AccountDao accountDAO = new AccountDao(hibernateBundle.getSessionFactory());

        final AccountMapper accountMapper = new AccountMapper();

        final AccountService accountService = new AccountService(accountDAO, accountMapper);

        SessionFactoryHealthCheck sessionFactoryHealthCheck = new SessionFactoryHealthCheck(hibernateBundle.getSessionFactory(), Optional.of("SELECT 1"));
        environment.healthChecks().register("Database Health Check", sessionFactoryHealthCheck);

        environment.jersey().register(new AccountController(accountService));
        environment.jersey().register(new BusinessException());

    }
}
