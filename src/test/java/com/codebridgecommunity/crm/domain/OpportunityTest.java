package com.codebridgecommunity.crm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.codebridgecommunity.crm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OpportunityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Opportunity.class);
        Opportunity opportunity1 = new Opportunity();
        opportunity1.setId(1L);
        Opportunity opportunity2 = new Opportunity();
        opportunity2.setId(opportunity1.getId());
        assertThat(opportunity1).isEqualTo(opportunity2);
        opportunity2.setId(2L);
        assertThat(opportunity1).isNotEqualTo(opportunity2);
        opportunity1.setId(null);
        assertThat(opportunity1).isNotEqualTo(opportunity2);
    }
}
