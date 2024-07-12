package com.pagbank.challenge;

import com.pagbank.challenge.infrastructure.cdborder.persistence.CdbOrderRepository;
import com.pagbank.challenge.infrastructure.customer.persistence.CustomerRepository;
import com.pagbank.challenge.infrastructure.product.persistence.ProductRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.List;

@Tag("integrationTest")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(
        basePackages = "com.pagbank.challenge",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")
        }
)
@ExtendWith(MySQLGatewayTest.CleanUpExtensions.class)
public @interface MySQLGatewayTest {
    class CleanUpExtensions implements BeforeEachCallback {

        @Override
        public void beforeEach(ExtensionContext extensionContext) throws Exception {
            final var appContext = SpringExtension
                    .getApplicationContext(extensionContext);

            cleanUp(List.of(
                    appContext.getBean(ProductRepository.class),
                    appContext.getBean(CustomerRepository.class),
                    appContext.getBean(CdbOrderRepository.class)

            ));
        }

        private void cleanUp(final Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }
}
