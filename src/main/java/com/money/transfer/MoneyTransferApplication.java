package com.money.transfer;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class MoneyTransferApplication extends Application<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {

    }
}
