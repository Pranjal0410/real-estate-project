package com.realestate.repository;

import com.realestate.model.entity.Portfolio;
import com.realestate.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PortfolioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PortfolioRepository portfolioRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .role(User.UserRole.INVESTOR)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .build();
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should save portfolio successfully")
    void save_Success() {
        Portfolio portfolio = Portfolio.builder()
                .user(testUser)
                .portfolioName("Test Portfolio")
                .riskProfile(Portfolio.RiskProfile.MODERATE)
                .status(Portfolio.PortfolioStatus.ACTIVE)
                .totalInvested(BigDecimal.ZERO)
                .totalCurrentValue(BigDecimal.ZERO)
                .build();

        Portfolio saved = portfolioRepository.save(portfolio);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getPortfolioName()).isEqualTo("Test Portfolio");
    }

    @Test
    @DisplayName("Should find portfolios by user")
    void findByUser_Success() {
        Portfolio portfolio1 = createAndPersistPortfolio("Portfolio 1");
        Portfolio portfolio2 = createAndPersistPortfolio("Portfolio 2");

        List<Portfolio> result = portfolioRepository.findByUser(testUser);

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should find portfolios by user and status")
    void findByUserAndStatus_Success() {
        createAndPersistPortfolio("Active Portfolio");
        Portfolio closed = createAndPersistPortfolio("Closed Portfolio");
        closed.setStatus(Portfolio.PortfolioStatus.CLOSED);
        entityManager.persist(closed);
        entityManager.flush();

        List<Portfolio> activePortfolios = portfolioRepository.findByUserAndStatus(
                testUser, Portfolio.PortfolioStatus.ACTIVE);

        assertThat(activePortfolios).hasSize(1);
        assertThat(activePortfolios.get(0).getPortfolioName()).isEqualTo("Active Portfolio");
    }

    @Test
    @DisplayName("Should find portfolio by user and name")
    void findByUserAndPortfolioName_Success() {
        createAndPersistPortfolio("My Portfolio");

        Optional<Portfolio> result = portfolioRepository.findByUserAndPortfolioName(testUser, "My Portfolio");

        assertThat(result).isPresent();
        assertThat(result.get().getPortfolioName()).isEqualTo("My Portfolio");
    }

    @Test
    @DisplayName("Should check if portfolio exists by user and name")
    void existsByUserAndPortfolioName_Success() {
        createAndPersistPortfolio("Existing Portfolio");

        boolean exists = portfolioRepository.existsByUserAndPortfolioName(testUser, "Existing Portfolio");
        boolean notExists = portfolioRepository.existsByUserAndPortfolioName(testUser, "Non Existing");

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should find active portfolios by user ID")
    void findActivePortfoliosByUserId_Success() {
        createAndPersistPortfolio("Active 1");
        createAndPersistPortfolio("Active 2");

        List<Portfolio> result = portfolioRepository.findActivePortfoliosByUserId(testUser.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should get total value by user")
    void getTotalValueByUser_Success() {
        Portfolio p1 = createAndPersistPortfolio("P1");
        p1.setTotalCurrentValue(new BigDecimal("100000"));
        entityManager.persist(p1);

        Portfolio p2 = createAndPersistPortfolio("P2");
        p2.setTotalCurrentValue(new BigDecimal("50000"));
        entityManager.persist(p2);
        entityManager.flush();

        Optional<BigDecimal> total = portfolioRepository.getTotalValueByUser(testUser);

        assertThat(total).isPresent();
        assertThat(total.get()).isEqualByComparingTo(new BigDecimal("150000"));
    }

    @Test
    @DisplayName("Should count active portfolios")
    void countActivePortfolios_Success() {
        createAndPersistPortfolio("P1");
        createAndPersistPortfolio("P2");

        long count = portfolioRepository.countActivePortfolios();

        assertThat(count).isEqualTo(2);
    }

    private Portfolio createAndPersistPortfolio(String name) {
        Portfolio portfolio = Portfolio.builder()
                .user(testUser)
                .portfolioName(name)
                .riskProfile(Portfolio.RiskProfile.MODERATE)
                .status(Portfolio.PortfolioStatus.ACTIVE)
                .totalInvested(BigDecimal.ZERO)
                .totalCurrentValue(BigDecimal.ZERO)
                .build();
        entityManager.persist(portfolio);
        entityManager.flush();
        return portfolio;
    }
}
