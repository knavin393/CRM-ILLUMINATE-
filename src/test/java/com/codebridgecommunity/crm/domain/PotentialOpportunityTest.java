package com.codebridgecommunity.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PotentialOpportunityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PotentialOpportunity.class);
        PotentialOpportunity potentialOpportunity1 = new PotentialOpportunity();
        potentialOpportunity1.setId(1L);
        PotentialOpportunity potentialOpportunity2 = new PotentialOpportunity();
        potentialOpportunity2.setId(potentialOpportunity1.getId());
        assertThat(potentialOpportunity1).isEqualTo(potentialOpportunity2);
        potentialOpportunity2.setId(2L);
        assertThat(potentialOpportunity1).isNotEqualTo(potentialOpportunity2);
        potentialOpportunity1.setId(null);
        assertThat(potentialOpportunity1).isNotEqualTo(potentialOpportunity2);
    }
}
